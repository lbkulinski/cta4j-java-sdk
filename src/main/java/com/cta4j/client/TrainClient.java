package com.cta4j.client;

import com.cta4j.exception.Cta4jException;
import com.cta4j.external.train.follow.CtaFollowResponse;
import com.cta4j.model.train.TrainCoordinates;
import com.cta4j.model.train.TrainLocation;
import com.cta4j.model.train.UpcomingTrainArrival;
import com.cta4j.util.HttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public final class TrainClient {
    private final String baseUrl;

    private final String apiKey;

    private final ObjectMapper objectMapper;

    private static final String DEFAULT_BASE_URL;

    private static final String FOLLOW_ENDPOINT;

    static {
        DEFAULT_BASE_URL = "https://lapi.transitchicago.com";

        FOLLOW_ENDPOINT = "/api/1.0/ttfollow.aspx";
    }

    public TrainClient(String baseUrl, String apiKey) {
        this.baseUrl = Objects.requireNonNull(baseUrl);

        this.apiKey = Objects.requireNonNull(apiKey);

        this.objectMapper = new ObjectMapper();
    }

    public TrainClient(String apiKey) {
        this(DEFAULT_BASE_URL, apiKey);
    }

    public TrainLocation getLocation(int run) {
        if (run <= 0) {
            throw new IllegalArgumentException("Run number must be a positive integer");
        }

        String url = "%s%s?runnumber=%d&key=%s".formatted(this.baseUrl, FOLLOW_ENDPOINT, run, this.apiKey);

        String response = HttpUtils.get(url);

        CtaFollowResponse followResponse;

        try {
            followResponse = this.objectMapper.readValue(response, CtaFollowResponse.class);
        } catch (IOException e) {
            String message = "Failed to parse response from %s".formatted(FOLLOW_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        TrainCoordinates coordinates = TrainCoordinates.fromExternal(followResponse.ctatt()
                                                                                   .position());

        List<UpcomingTrainArrival> arrivals = followResponse.ctatt()
                                                            .eta()
                                                            .stream()
                                                            .map(UpcomingTrainArrival::fromExternal)
                                                            .toList();

        return new TrainLocation(coordinates, arrivals);
    }
}
