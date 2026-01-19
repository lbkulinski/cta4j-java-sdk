package com.cta4j.train.client.internal;

import com.cta4j.train.client.TrainClient;
import com.cta4j.common.exception.Cta4jException;
import com.cta4j.train.external.arrival.CtaArrivalsCtatt;
import com.cta4j.train.external.arrival.CtaArrivalsEta;
import com.cta4j.train.external.arrival.CtaArrivalsResponse;
import com.cta4j.train.external.follow.CtaFollowCtatt;
import com.cta4j.train.external.follow.CtaFollowEta;
import com.cta4j.train.external.follow.CtaFollowPosition;
import com.cta4j.train.external.follow.CtaFollowResponse;
import com.cta4j.train.mapper.StationArrivalMapper;
import com.cta4j.train.mapper.TrainCoordinatesMapper;
import com.cta4j.train.mapper.UpcomingTrainArrivalMapper;
import com.cta4j.train.model.TrainCoordinates;
import com.cta4j.train.model.Train;
import com.cta4j.train.model.UpcomingTrainArrival;
import com.cta4j.train.model.StationArrival;
import com.cta4j.common.util.HttpUtils;
import org.apache.hc.core5.net.URIBuilder;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.Optional;

@NullMarked
@ApiStatus.Internal
@SuppressWarnings("ConstantConditions")
public final class TrainClientImpl implements TrainClient {
    private static final String DEFAULT_HOST = "lapi.transitchicago.com";
    private static final String ARRIVALS_ENDPOINT = "/api/1.0/ttarrivals.aspx";
    private static final String FOLLOW_ENDPOINT = "/api/1.0/ttfollow.aspx";

    private final String host;
    private final String apiKey;
    private final ObjectMapper objectMapper;

    private TrainClientImpl(String host, String apiKey) {
        if (host == null) {
            throw new IllegalArgumentException("host must not be null");
        }

        if (apiKey == null) {
            throw new IllegalArgumentException("apiKey must not be null");
        }

        this.host = host;
        this.apiKey = apiKey;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public List<StationArrival> getStationArrivals(String stationId) {
        if (stationId == null) {
            throw new IllegalArgumentException("stationId must not be null");
        }

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
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(ARRIVALS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaArrivalsCtatt ctatt = arrivalsResponse.ctatt();

        if (ctatt == null) {
            throw new Cta4jException("Invalid response from %s".formatted(ARRIVALS_ENDPOINT));
        }

        List<CtaArrivalsEta> eta = ctatt.eta();

        if ((eta == null) || eta.isEmpty()) {
            return List.of();
        }

        return eta.stream()
                  .map(StationArrivalMapper::fromExternal)
                  .toList();
    }

    @Override
    public Optional<Train> getTrain(String run) {
        if (run == null) {
            throw new IllegalArgumentException("run must not be null");
        }

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
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(FOLLOW_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaFollowCtatt ctatt = followResponse.ctatt();

        if (ctatt == null) {
            throw new Cta4jException("Invalid response from %s".formatted(FOLLOW_ENDPOINT));
        }

        TrainCoordinates coordinates = null;
        CtaFollowPosition position = ctatt.position();

        if (position != null) {
            coordinates = TrainCoordinatesMapper.fromExternal(position);
        }

        List<CtaFollowEta> eta = ctatt.eta();

        if ((eta == null) || eta.isEmpty()) {
            if (coordinates == null) {
                return Optional.empty();
            }

            Train train = new Train(coordinates, List.of());

            return Optional.of(train);
        }

        List<UpcomingTrainArrival> arrivals = eta.stream()
                                                 .map(UpcomingTrainArrivalMapper::fromExternal)
                                                 .toList();

        Train train = new Train(coordinates, arrivals);

        return Optional.of(train);
    }

    public static final class BuilderImpl implements TrainClient.Builder {
        @Nullable
        private String host;

        @Nullable
        private String apiKey;

        public BuilderImpl() {
            this.host = null;
            this.apiKey = null;
        }

        @Override
        public Builder host(String host) {
            if (host == null) {
                throw new IllegalArgumentException("host must not be null");
            }

            this.host = host;

            return this;
        }

        @Override
        public Builder apiKey(String apiKey) {
            if (apiKey == null) {
                throw new IllegalArgumentException("apiKey must not be null");
            }

            this.apiKey = apiKey;

            return this;
        }

        @Override
        public TrainClient build() {
            String finalHost = (this.host == null) ? DEFAULT_HOST : this.host;

            if (this.apiKey == null) {
                throw new IllegalStateException("API key must not be null");
            }

            return new TrainClientImpl(finalHost, this.apiKey);
        }
    }
}
