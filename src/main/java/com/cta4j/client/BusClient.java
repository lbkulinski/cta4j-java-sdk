package com.cta4j.client;

import com.cta4j.exception.Cta4jException;
import com.cta4j.external.bus.route.CtaRoutesResponse;
import com.cta4j.external.bus.stop.CtaStopsResponse;
import com.cta4j.model.bus.Route;
import com.cta4j.model.bus.Stop;
import com.cta4j.util.HttpUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public final class BusClient {
    private final String baseUrl;

    private final String apiKey;

    private final ObjectMapper objectMapper;

    private static final String DEFAULT_BASE_URL;

    private static final String ROUTES_ENDPOINT;

    private static final String DIRECTIONS_ENDPOINT;

    private static final String STOPS_ENDPOINT;

    static {
        DEFAULT_BASE_URL = "https://ctabustracker.com";

        ROUTES_ENDPOINT = "/bustime/api/v3/getroutes";

        DIRECTIONS_ENDPOINT = "/bustime/api/v3/getdirections";

        STOPS_ENDPOINT = "/bustime/api/v3/getstops";
    }

    public BusClient(String baseUrl, String apiKey) {
        this.baseUrl = Objects.requireNonNull(baseUrl);

        this.apiKey = Objects.requireNonNull(apiKey);

        this.objectMapper = new ObjectMapper();
    }

    public BusClient(String apiKey) {
        this(DEFAULT_BASE_URL, apiKey);
    }

    public List<Route> getRoutes() {
        String url = String.format("%s%s?key=%s&format=json", this.baseUrl, ROUTES_ENDPOINT, this.apiKey);

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
                             .map(Route::fromExternal)
                             .toList();
    }

    public List<String> getDirections(String routeId) {
        Objects.requireNonNull(routeId);

        String url = String.format("%s%s?rt=%s&key=%s&format=json", this.baseUrl, DIRECTIONS_ENDPOINT, routeId,
            this.apiKey);

        String response = HttpUtils.get(url);

        TypeReference<List<String>> typeReference = new TypeReference<>() {};

        List<String> directions;

        try {
            directions = this.objectMapper.readValue(response, typeReference);
        } catch (IOException e) {
            String message = "Failed to parse response from %s".formatted(DIRECTIONS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        return List.copyOf(directions);
    }

    public List<Stop> getStops(String routeId, String direction) {
        Objects.requireNonNull(routeId);

        Objects.requireNonNull(direction);

        String url = String.format("%s%s?rt=%s&dir=%s&key=%s&format=json", this.baseUrl, STOPS_ENDPOINT, routeId,
            direction, this.apiKey);

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
                            .map(Stop::fromExternal)
                            .toList();
    }

//    @RequestLine("GET /bustime/api/v3/getpredictions?rt={routeId}&stpid={stopId}")
//    CtaPredictionsResponse getPredictions(String routeId, int stopId);
//
//    @RequestLine("GET /bustime/api/v3/getdetours?rt={routeId}&rtdir={direction}")
//    CtaDetoursResponse getDetours(String routeId, String direction);
//
//    @RequestLine("GET /bustime/api/v3/getvehicles?vid={id}")
//    CtaVehicleResponse getVehicle(String id);
}
