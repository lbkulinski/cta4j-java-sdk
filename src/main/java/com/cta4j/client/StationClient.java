package com.cta4j.client;

import com.cta4j.exception.Cta4jException;
import com.cta4j.external.train.arrival.CtaArrivalsResponse;
import com.cta4j.model.trainstation.StationArrival;
import com.cta4j.util.HttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public final class StationClient {
    private final String baseUrl;

    private final String apiKey;

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

        this.objectMapper = new ObjectMapper();
    }

    public StationClient(String apiKey) {
        this(DEFAULT_BASE_URL, apiKey);
    }

    public List<StationArrival> getArrivals(int stationId) {
        if (stationId <= 0) {
            throw new IllegalArgumentException("Station ID must be a positive integer");
        }

        String url = "%s%s?mapid=%d&key=%s".formatted(this.baseUrl, ARRIVALS_ENDPOINT, stationId, this.apiKey);

        String response = HttpUtils.get(url);

        CtaArrivalsResponse arrivalsResponse;

        try {
            arrivalsResponse = this.objectMapper.readValue(response, CtaArrivalsResponse.class);
        } catch (IOException e) {
            String message = "Failed to parse response from %s".formatted(ARRIVALS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        return arrivalsResponse.ctatt()
                               .eta()
                               .stream()
                               .map(StationArrival::fromExternal)
                               .toList();
    }
}
