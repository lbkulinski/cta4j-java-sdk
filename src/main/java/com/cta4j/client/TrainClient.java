package com.cta4j.client;

import com.cta4j.exception.Cta4jException;
import com.cta4j.external.train.arrival.CtaArrivalsCtatt;
import com.cta4j.external.train.arrival.CtaArrivalsEta;
import com.cta4j.external.train.arrival.CtaArrivalsResponse;
import com.cta4j.external.train.follow.CtaFollowCtatt;
import com.cta4j.external.train.follow.CtaFollowEta;
import com.cta4j.external.train.follow.CtaFollowPosition;
import com.cta4j.external.train.follow.CtaFollowResponse;
import com.cta4j.mapper.train.StationArrivalMapper;
import com.cta4j.mapper.train.TrainCoordinatesMapper;
import com.cta4j.mapper.train.UpcomingTrainArrivalMapper;
import com.cta4j.model.train.TrainCoordinates;
import com.cta4j.model.train.Train;
import com.cta4j.model.train.UpcomingTrainArrival;
import com.cta4j.model.train.StationArrival;
import com.cta4j.util.HttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A client for interacting with the CTA Train Tracker API.
 */
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

    /**
     * Constructs a new {@code TrainClient} with the specified host and API key.
     *
     * @param host the host of the CTA Train Tracker API
     * @param apiKey the API key for accessing the CTA Train Tracker API
     * @throws NullPointerException if host or API key is {@code null}
     */
    public TrainClient(String host, String apiKey) {
        this.host = Objects.requireNonNull(host);

        this.apiKey = Objects.requireNonNull(apiKey);

        this.objectMapper = new ObjectMapper();
    }

    /**
     * Constructs a new {@code TrainClient} with the default host and the specified API key.
     *
     * @param apiKey the API key for accessing the CTA Train Tracker API
     * @throws NullPointerException if API key is {@code null}
     */
    public TrainClient(String apiKey) {
        this(DEFAULT_HOST, apiKey);
    }

    /**
     * Retrieves a {@code List} of upcoming arrivals for a specific station.
     *
     * @param stationId the ID of the station
     * @return a {@code List} of upcoming arrivals for the specified station
     * @throws NullPointerException if the specified station ID is {@code null}
     * @throws Cta4jException if an error occurs while fetching the data
     */
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

    /**
     * Retrieves information about a specific train by its run number.
     *
     * @param run the run number of the train
     * @return an {@code Optional} containing the train information if found, or an empty {@code Optional} if not found
     * @throws NullPointerException if the specified run number is {@code null}
     * @throws Cta4jException if an error occurs while fetching the data
     */
    public Optional<Train> getTrain(String run) {
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

        CtaFollowCtatt ctatt = followResponse.ctatt();

        if (ctatt == null) {
            throw new Cta4jException("Invalid response from %s".formatted(FOLLOW_ENDPOINT));
        }

        CtaFollowPosition position = ctatt.position();

        if (position == null) {
            return Optional.empty();
        }

        TrainCoordinates coordinates = TrainCoordinatesMapper.fromExternal(position);

        List<CtaFollowEta> eta = ctatt.eta();

        if ((eta == null) || eta.isEmpty()) {
            Train train = new Train(coordinates, List.of());

            return Optional.of(train);
        }

        List<UpcomingTrainArrival> arrivals = eta.stream()
                                                 .map(UpcomingTrainArrivalMapper::fromExternal)
                                                 .toList();

        Train train = new Train(coordinates, arrivals);

        return Optional.of(train);
    }
}
