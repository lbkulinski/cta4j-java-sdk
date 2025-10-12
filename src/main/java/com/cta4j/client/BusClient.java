package com.cta4j.client;

import com.cta4j.exception.Cta4jException;
import com.cta4j.external.bus.detour.CtaDetoursResponse;
import com.cta4j.external.bus.direction.CtaDirection;
import com.cta4j.external.bus.direction.CtaDirectionsResponse;
import com.cta4j.external.bus.prediction.CtaPredictionsResponse;
import com.cta4j.external.bus.route.CtaRoutesResponse;
import com.cta4j.external.bus.stop.CtaStopsResponse;
import com.cta4j.external.bus.vehicle.CtaVehicleResponse;
import com.cta4j.mapper.bus.*;
import com.cta4j.model.bus.*;
import com.cta4j.util.HttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A client for interacting with the CTA Bus Tracker API.
 */
public final class BusClient {
    private final String host;

    private final String apiKey;

    private final ObjectMapper objectMapper;

    private static final String DEFAULT_HOST;

    private static final String ROUTES_ENDPOINT;

    private static final String DIRECTIONS_ENDPOINT;

    private static final String STOPS_ENDPOINT;

    private static final String PREDICTIONS_ENDPOINT;

    private static final String DETOURS_ENDPOINT;

    private static final String VEHICLES_ENDPOINT;

    static {
        DEFAULT_HOST = "ctabustracker.com";

        ROUTES_ENDPOINT = "/bustime/api/v3/getroutes";

        DIRECTIONS_ENDPOINT = "/bustime/api/v3/getdirections";

        STOPS_ENDPOINT = "/bustime/api/v3/getstops";

        PREDICTIONS_ENDPOINT = "/bustime/api/v3/getpredictions";

        DETOURS_ENDPOINT = "/bustime/api/v3/getdetours";

        VEHICLES_ENDPOINT = "/bustime/api/v3/getvehicles";
    }

    /**
     * Constructs a new {@code BusClient} with the specified host and API key.
     *
     * @param host the host of the CTA Bus Tracker API
     * @param apiKey the API key for accessing the CTA Bus Tracker API
     * @throws NullPointerException if the specified host or API key is {@code null}
     */
    public BusClient(String host, String apiKey) {
        this.host = Objects.requireNonNull(host);

        this.apiKey = Objects.requireNonNull(apiKey);

        this.objectMapper = new ObjectMapper();
    }

    /**
     * Constructs a new {@code BusClient} with the default host and the specified API key.
     *
     * @param apiKey the API key for accessing the CTA Bus Tracker API
     * @throws NullPointerException if API key is {@code null}
     */
    public BusClient(String apiKey) {
        this(DEFAULT_HOST, apiKey);
    }

    /**
     * Retrieves a {@code List} of all bus routes.
     *
     * @return a {@code List} of all bus routes
     * @throws Cta4jException if an error occurs while fetching the data
     */
    public List<Route> getRoutes() {
        String url = new URIBuilder()
            .setScheme("https")
            .setHost(this.host)
            .setPath(ROUTES_ENDPOINT)
            .addParameter("key", this.apiKey)
            .addParameter("format", "json")
            .toString();

        String response = HttpUtils.get(url);

        CtaRoutesResponse routesResponse;

        try {
            routesResponse = this.objectMapper.readValue(response, CtaRoutesResponse.class);
        } catch (IOException e) {
            String message = "Failed to parse response from %s".formatted(ROUTES_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        return routesResponse.bustimeResponse()
                             .routes()
                             .stream()
                             .map(RouteMapper::fromExternal)
                             .toList();
    }

    /**
     * Retrieves a {@code List} of directions for a specific bus route.
     *
     * @param routeId the ID of the bus route
     * @return a {@code List} of directions for the specified bus route
     * @throws NullPointerException if the specified bus route is {@code null}
     * @throws Cta4jException if an error occurs while fetching the data
     */
    public List<String> getDirections(String routeId) {
        Objects.requireNonNull(routeId);

        String url = new URIBuilder()
            .setScheme("https")
            .setHost(this.host)
            .setPath(DIRECTIONS_ENDPOINT)
            .addParameter("rt", routeId)
            .addParameter("key", this.apiKey)
            .addParameter("format", "json")
            .toString();

        String response = HttpUtils.get(url);

        CtaDirectionsResponse directionsResponse;

        try {
            directionsResponse = this.objectMapper.readValue(response, CtaDirectionsResponse.class);
        } catch (IOException e) {
            String message = "Failed to parse response from %s".formatted(DIRECTIONS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        return directionsResponse.bustimeResponse()
                                 .directions()
                                 .stream()
                                 .map(CtaDirection::id)
                                 .toList();
    }

    /**
     * Retrieves a {@code List} of stops for a specific bus route and direction.
     *
     * @param routeId the ID of the bus route
     * @param direction the direction of the bus route
     * @return a {@code List} of stops for the specified bus route and direction
     * @throws NullPointerException if the specified bus route or direction is {@code null}
     * @throws Cta4jException if an error occurs while fetching the data
     */
    public List<Stop> getStops(String routeId, String direction) {
        Objects.requireNonNull(routeId);

        Objects.requireNonNull(direction);

        String url = new URIBuilder()
            .setScheme("https")
            .setHost(this.host)
            .setPath(STOPS_ENDPOINT)
            .addParameter("rt", routeId)
            .addParameter("dir", direction)
            .addParameter("key", this.apiKey)
            .addParameter("format", "json")
            .toString();

        String response = HttpUtils.get(url);

        CtaStopsResponse stopsResponse;

        try {
            stopsResponse = this.objectMapper.readValue(response, CtaStopsResponse.class);
        } catch (IOException e) {
            String message = "Failed to parse response from %s".formatted(STOPS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        return stopsResponse.bustimeResponse()
                            .stops()
                            .stream()
                            .map(StopMapper::fromExternal)
                            .toList();
    }

    /**
     * Retrieves a {@code List} of upcoming arrivals for a specific bus route and stop.
     *
     * @param routeId the ID of the bus route
     * @param stopId the ID of the bus stop
     * @return a {@code List} of upcoming arrivals for the specified bus route and stop
     * @throws NullPointerException if the specified bus route or stop is {@code null}
     * @throws Cta4jException if an error occurs while fetching the data
     */
    public List<StopArrival> getStopArrivals(String routeId, String stopId) {
        Objects.requireNonNull(routeId);

        Objects.requireNonNull(stopId);

        String url = new URIBuilder()
            .setScheme("https")
            .setHost(this.host)
            .setPath(PREDICTIONS_ENDPOINT)
            .addParameter("rt", routeId)
            .addParameter("stpid", stopId)
            .addParameter("key", this.apiKey)
            .addParameter("format", "json")
            .toString();

        String response = HttpUtils.get(url);

        CtaPredictionsResponse predictionsResponse;

        try {
            predictionsResponse = this.objectMapper.readValue(response, CtaPredictionsResponse.class);
        } catch (IOException e) {
            String message = "Failed to parse response from %s".formatted(PREDICTIONS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        return predictionsResponse.bustimeResponse()
                                  .prd()
                                  .stream()
                                  .map(StopArrivalMapper::fromExternal)
                                  .toList();
    }

    /**
     * Retrieves a {@code List} of detours for a specific bus route and direction.
     *
     * @param routeId the ID of the bus route
     * @param direction the direction of the bus route
     * @return a {@code List} of detours for the specified bus route and direction
     * @throws NullPointerException if the specified bus route or direction is {@code null}
     * @throws Cta4jException if an error occurs while fetching the data
     */
    public List<Detour> getDetours(String routeId, String direction) {
        Objects.requireNonNull(routeId);

        Objects.requireNonNull(direction);

        String url = new URIBuilder()
            .setScheme("https")
            .setHost(this.host)
            .setPath(DETOURS_ENDPOINT)
            .addParameter("rt", routeId)
            .addParameter("dir", direction)
            .addParameter("key", this.apiKey)
            .addParameter("format", "json")
            .toString();

        String response = HttpUtils.get(url);

        CtaDetoursResponse detoursResponse;

        try {
            detoursResponse = this.objectMapper.readValue(response, CtaDetoursResponse.class);
        } catch (IOException e) {
            String message = "Failed to parse response from %s".formatted(DETOURS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        return detoursResponse.bustimeResponse()
                              .dtrs()
                              .stream()
                              .map(DetourMapper::fromExternal)
                              .toList();
    }

    /**
     * Retrieves information about a specific bus by its ID.
     *
     * @param id the ID of the bus
     * @return an {@code Optional} containing the bus information if found, or an empty {@code Optional} if not found
     * @throws NullPointerException if the specified bus ID is {@code null}
     * @throws Cta4jException if an error occurs while fetching the data
     */
    public Optional<Bus> getBus(String id) {
        Objects.requireNonNull(id);

        String url = new URIBuilder()
            .setScheme("https")
            .setHost(this.host)
            .setPath(VEHICLES_ENDPOINT)
            .addParameter("vid", id)
            .addParameter("key", this.apiKey)
            .addParameter("format", "json")
            .toString();

        String response = HttpUtils.get(url);

        CtaVehicleResponse vehicleResponse;

        try {
            vehicleResponse = this.objectMapper.readValue(response, CtaVehicleResponse.class);
        } catch (IOException e) {
            String message = "Failed to parse response from %s".formatted(VEHICLES_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        return vehicleResponse.bustimeResponse()
                              .vehicle()
                              .stream()
                              .map(BusMapper::fromExternal)
                              .findFirst();
    }
}
