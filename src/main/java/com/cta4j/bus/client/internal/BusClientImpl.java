package com.cta4j.bus.client.internal;

import com.cta4j.bus.client.BusClient;
import com.cta4j.bus.mapper.*;
import com.cta4j.bus.model.*;
import com.cta4j.exception.Cta4jException;
import com.cta4j.bus.external.detour.CtaDetour;
import com.cta4j.bus.external.detour.CtaDetoursBustimeResponse;
import com.cta4j.bus.external.detour.CtaDetoursResponse;
import com.cta4j.bus.external.direction.CtaDirection;
import com.cta4j.bus.external.direction.CtaDirectionsBustimeResponse;
import com.cta4j.bus.external.direction.CtaDirectionsResponse;
import com.cta4j.bus.external.prediction.CtaPredictionsBustimeResponse;
import com.cta4j.bus.external.prediction.CtaPredictionsPrd;
import com.cta4j.bus.external.prediction.CtaPredictionsResponse;
import com.cta4j.bus.external.route.CtaRoute;
import com.cta4j.bus.external.route.CtaRoutesBustimeResponse;
import com.cta4j.bus.external.route.CtaRoutesResponse;
import com.cta4j.bus.external.stop.CtaStop;
import com.cta4j.bus.external.stop.CtaStopsBustimeResponse;
import com.cta4j.bus.external.stop.CtaStopsResponse;
import com.cta4j.bus.external.vehicle.CtaVehicle;
import com.cta4j.bus.external.vehicle.CtaVehicleBustimeResponse;
import com.cta4j.bus.external.vehicle.CtaVehicleResponse;
import com.cta4j.util.HttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ApiStatus.Internal
public final class BusClientImpl implements BusClient {
    private final String host;

    private final String apiKey;

    private final ObjectMapper objectMapper;

    private static final String DEFAULT_HOST = "ctabustracker.com";

    private static final String ROUTES_ENDPOINT = "/bustime/api/v3/getroutes";

    private static final String DIRECTIONS_ENDPOINT = "/bustime/api/v3/getdirections";

    private static final String STOPS_ENDPOINT = "/bustime/api/v3/getstops";

    private static final String PREDICTIONS_ENDPOINT = "/bustime/api/v3/getpredictions";

    private static final String DETOURS_ENDPOINT = "/bustime/api/v3/getdetours";

    private static final String VEHICLES_ENDPOINT = "/bustime/api/v3/getvehicles";

    private BusClientImpl(String host, String apiKey) {
        this.host = Objects.requireNonNull(host);

        this.apiKey = Objects.requireNonNull(apiKey);

        this.objectMapper = new ObjectMapper();
    }

    @Override
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

        CtaRoutesBustimeResponse bustimeResponse = routesResponse.bustimeResponse();

        if (bustimeResponse == null) {
            throw new Cta4jException("Invalid response from %s".formatted(ROUTES_ENDPOINT));
        }

        List<CtaRoute> routes = bustimeResponse.routes();

        if ((routes == null) || routes.isEmpty()) {
            return List.of();
        }

        return routes.stream()
                     .map(RouteMapper::fromExternal)
                     .toList();
    }

    @Override
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

        CtaDirectionsBustimeResponse bustimeResponse = directionsResponse.bustimeResponse();

        if (bustimeResponse == null) {
            throw new Cta4jException("Invalid response from %s".formatted(DIRECTIONS_ENDPOINT));
        }

        List<CtaDirection> directions = bustimeResponse.directions();

        if ((directions == null) || directions.isEmpty()) {
            return List.of();
        }

        return directions.stream()
                         .map(CtaDirection::id)
                         .toList();
    }

    @Override
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

        CtaStopsBustimeResponse bustimeResponse = stopsResponse.bustimeResponse();

        if (bustimeResponse == null) {
            throw new Cta4jException("Invalid response from %s".formatted(STOPS_ENDPOINT));
        }

        List<CtaStop> stops = bustimeResponse.stops();

        if ((stops == null) || stops.isEmpty()) {
            return List.of();
        }

        return stops.stream()
                    .map(StopMapper::fromExternal)
                    .toList();
    }

    @Override
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

        CtaPredictionsBustimeResponse bustimeResponse = predictionsResponse.bustimeResponse();

        if (bustimeResponse == null) {
            throw new Cta4jException("Invalid response from %s".formatted(PREDICTIONS_ENDPOINT));
        }

        List<CtaPredictionsPrd> prd = bustimeResponse.prd();

        if ((prd == null) || prd.isEmpty()) {
            return List.of();
        }

        return prd.stream()
                  .map(StopArrivalMapper::fromExternal)
                  .toList();
    }

    @Override
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

        CtaDetoursBustimeResponse bustimeResponse = detoursResponse.bustimeResponse();

        if (bustimeResponse == null) {
            throw new Cta4jException("Invalid response from %s".formatted(DETOURS_ENDPOINT));
        }

        List<CtaDetour> dtrs = bustimeResponse.dtrs();

        if ((dtrs == null) || dtrs.isEmpty()) {
            return List.of();
        }

        return dtrs.stream()
                   .map(DetourMapper::fromExternal)
                   .toList();
    }

    @Override
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

        CtaVehicleBustimeResponse bustimeResponse = vehicleResponse.bustimeResponse();

        if (bustimeResponse == null) {
            throw new Cta4jException("Invalid response from %s".formatted(VEHICLES_ENDPOINT));
        }

        List<CtaVehicle> vehicles = bustimeResponse.vehicle();

        if ((vehicles == null) || vehicles.isEmpty()) {
            return Optional.empty();
        }

        return vehicles.stream()
                       .map(BusMapper::fromExternal)
                       .findFirst();
    }

    public static final class BuilderImpl implements BusClient.Builder {
        private String host;

        private String apiKey;

        public BuilderImpl() {
            this.host = null;

            this.apiKey = null;
        }

        @Override
        public Builder host(String host) {
            this.host = host;

            return this;
        }

        @Override
        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;

            return this;
        }

        @Override
        public BusClient build() {
            String finalHost = (this.host == null) ? DEFAULT_HOST : this.host;

            if (this.apiKey == null) {
                throw new NullPointerException("API key must not be null");
            }

            return new BusClientImpl(finalHost, this.apiKey);
        }
    }
}
