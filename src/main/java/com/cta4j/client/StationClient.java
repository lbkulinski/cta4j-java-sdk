package com.cta4j.client;

import com.cta4j.exception.Cta4jException;
import com.cta4j.external.train.arrival.CtaArrivalsResponse;
import com.cta4j.model.trainstation.StationArrival;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.failsafe.RetryPolicy;
import dev.failsafe.okhttp.FailsafeCall;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public final class StationClient {
    private final String baseUrl;

    private final String apiKey;

    private final OkHttpClient httpClient;

    private final ObjectMapper objectMapper;

    private static final String DEFAULT_BASE_URL;

    private static final String ARRIVALS_ENDPOINT;

    static {
        DEFAULT_BASE_URL = "https://lapi.transitchicago.com";

        ARRIVALS_ENDPOINT = "/api/1.0/ttarrivals.aspx";
    }

    public StationClient(String baseUrl, String apiKey) {
        this.baseUrl = Objects.requireNonNull(baseUrl);

        this.apiKey = Objects.requireNonNull(apiKey);

        this.httpClient = new OkHttpClient();

        this.objectMapper = new ObjectMapper();
    }

    public StationClient(String apiKey) {
        this(DEFAULT_BASE_URL, apiKey);
    }

    public List<StationArrival> getArrivals(int stationId) {
        if (stationId <= 0) {
            throw new IllegalArgumentException("Station ID must be a positive integer");
        }

        String urlString = "%s%s?mapid=%d&key=%s".formatted(this.baseUrl, ARRIVALS_ENDPOINT, stationId, this.apiKey);

        HttpUrl httpUrl = HttpUrl.parse(urlString);

        if (httpUrl == null) {
            throw new Cta4jException("Failed to parse URL");
        }

        RetryPolicy<Response> retryPolicy = RetryPolicy.ofDefaults();

        Request request = new Request.Builder()
            .get()
            .url(httpUrl)
            .addHeader("Accept", "application/json")
            .build();

        Call call = this.httpClient.newCall(request);

        FailsafeCall failsafeCall = FailsafeCall.with(retryPolicy)
                                                .compose(call);

        int statusCode;

        String body;

        try (Response response = failsafeCall.execute()) {
            statusCode = response.code();

            body = response.body()
                           .string();
        } catch (IOException e) {
            String message = "Failed to send GET request to %s".formatted(ARRIVALS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        if (statusCode != 200) {
            String message = "Received HTTP %d from %s".formatted(statusCode, ARRIVALS_ENDPOINT);

            throw new Cta4jException(message);
        }

        CtaArrivalsResponse arrivalsResponse;

        try {
            arrivalsResponse = this.objectMapper.readValue(body, CtaArrivalsResponse.class);
        } catch (IOException e) {
            String message = "Failed to parse response body from %s".formatted(ARRIVALS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        return arrivalsResponse.ctatt()
                               .eta()
                               .stream()
                               .map(StationArrival::fromExternal)
                               .toList();
    }
//
//    @RequestLine("GET /api/1.0/ttfollow.aspx?runnumber={run}")
//    public CtaFollowResponse followTrain(int run) {
//        if (run <= 0) {
//            throw new IllegalArgumentException("Run number must be a positive integer");
//        }
//
//        return null;
//    }
}
