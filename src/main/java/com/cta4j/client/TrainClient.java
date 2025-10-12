package com.cta4j.client;

import com.cta4j.exception.Cta4jException;
import com.cta4j.external.train.arrival.CtaArrivalsResponse;
import com.cta4j.external.train.follow.CtaFollowResponse;
import com.cta4j.mapper.train.StationArrivalMapper;
import com.cta4j.mapper.train.TrainCoordinatesMapper;
import com.cta4j.mapper.train.UpcomingTrainArrivalMapper;
import com.cta4j.model.train.TrainCoordinates;
import com.cta4j.model.train.TrainLocation;
import com.cta4j.model.train.UpcomingTrainArrival;
import com.cta4j.model.train.StationArrival;
import com.cta4j.util.HttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public final class TrainClient {
    private final String host;

    private final String apiKey;

    private final ObjectMapper objectMapper;

    private static final String DEFAULT_HOST;

    private static final String ARRIVALS_ENDPOINT;

    private static final String FOLLOW_ENDPOINT;

    static {
        DEFAULT_HOST = "lapi.transitchicago.com";

        ARRIVALS_ENDPOINT = "/api/1.0/ttarrivals.aspx";

        FOLLOW_ENDPOINT = "/api/1.0/ttfollow.aspx";
    }

    public TrainClient(String host, String apiKey) {
        this.host = Objects.requireNonNull(host);

        this.apiKey = Objects.requireNonNull(apiKey);

        this.objectMapper = new ObjectMapper();
    }

    public TrainClient(String apiKey) {
        this(DEFAULT_HOST, apiKey);
    }

    public List<StationArrival> getStationArrivals(String stationId) {
        Objects.requireNonNull(stationId);

        String url = new URIBuilder()
            .setScheme("https")
            .setHost(this.host)
            .setPath(ARRIVALS_ENDPOINT)
            .addParameter("mapid", stationId)
            .addParameter("key", this.apiKey)
            .addParameter("outputType", "JSON")
            .toString();

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
                               .map(StationArrivalMapper::fromExternal)
                               .toList();
    }

    public TrainLocation getTrainLocation(String run) {
        Objects.requireNonNull(run);

        String url = new URIBuilder()
            .setScheme("https")
            .setHost(this.host)
            .setPath(FOLLOW_ENDPOINT)
            .addParameter("runnumber", run)
            .addParameter("key", this.apiKey)
            .addParameter("outputType", "JSON")
            .toString();

        String response = HttpUtils.get(url);

        CtaFollowResponse followResponse;

        try {
            followResponse = this.objectMapper.readValue(response, CtaFollowResponse.class);
        } catch (IOException e) {
            String message = "Failed to parse response from %s".formatted(FOLLOW_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        TrainCoordinates coordinates = TrainCoordinatesMapper.fromExternal(followResponse.ctatt()
                                                                                         .position());

        List<UpcomingTrainArrival> arrivals = followResponse.ctatt()
                                                            .eta()
                                                            .stream()
                                                            .map(UpcomingTrainArrivalMapper::fromExternal)
                                                            .toList();

        return new TrainLocation(coordinates, arrivals);
    }
}
