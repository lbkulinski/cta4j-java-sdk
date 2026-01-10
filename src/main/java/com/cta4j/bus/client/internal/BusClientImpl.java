package com.cta4j.bus.client.internal;

import com.cta4j.bus.client.BusClient;
import com.cta4j.bus.external.CtaBustimeResponse;
import com.cta4j.bus.external.CtaError;
import com.cta4j.bus.external.CtaResponse;
import com.cta4j.bus.external.CtaPrediction;
import com.cta4j.bus.mapper.ArrivalMapper;
import com.cta4j.bus.mapper.BusMapper;
import com.cta4j.bus.mapper.DetourMapper;
import com.cta4j.bus.mapper.RouteMapper;
import com.cta4j.bus.mapper.StopMapper;
import com.cta4j.bus.model.Arrival;
import com.cta4j.bus.model.Bus;
import com.cta4j.bus.model.Detour;
import com.cta4j.bus.model.Route;
import com.cta4j.bus.model.Stop;
import com.cta4j.exception.Cta4jException;
import com.cta4j.bus.external.CtaDetour;
import com.cta4j.bus.external.CtaDirection;
import com.cta4j.bus.external.CtaRoute;
import com.cta4j.bus.external.CtaStop;
import com.cta4j.bus.external.CtaVehicle;
import com.cta4j.util.HttpUtils;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.mapstruct.factory.Mappers;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.Optional;

@NullMarked
@ApiStatus.Internal
@SuppressWarnings("ConstantConditions")
public final class BusClientImpl implements BusClient {
    private static final String DEFAULT_HOST = "ctabustracker.com";
    private static final String ROUTES_ENDPOINT = "/bustime/api/v3/getroutes";
    private static final String DIRECTIONS_ENDPOINT = "/bustime/api/v3/getdirections";
    private static final String STOPS_ENDPOINT = "/bustime/api/v3/getstops";
    private static final String PREDICTIONS_ENDPOINT = "/bustime/api/v3/getpredictions";
    private static final String DETOURS_ENDPOINT = "/bustime/api/v3/getdetours";
    private static final String VEHICLES_ENDPOINT = "/bustime/api/v3/getvehicles";

    private final String host;
    private final String apiKey;
    private final ObjectMapper objectMapper;
    private final ArrivalMapper arrivalMapper;
    private final RouteMapper routeMapper;
    private final StopMapper stopMapper;
    private final DetourMapper detourMapper;
    private final BusMapper busMapper;

    private BusClientImpl(String host, String apiKey) {
        if (host == null) {
            throw new IllegalArgumentException("host must not be null");
        }

        if (apiKey == null) {
            throw new IllegalArgumentException("apiKey must not be null");
        }

        this.host = host;
        this.apiKey = apiKey;
        this.objectMapper = new ObjectMapper();
        this.arrivalMapper = Mappers.getMapper(ArrivalMapper.class);
        this.routeMapper = Mappers.getMapper(RouteMapper.class);
        this.stopMapper = Mappers.getMapper(StopMapper.class);
        this.detourMapper = Mappers.getMapper(DetourMapper.class);
        this.busMapper = Mappers.getMapper(BusMapper.class);
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

        TypeReference<CtaResponse<CtaRoute>> typeReference = new TypeReference<>() {};
        CtaResponse<CtaRoute> routesResponse;

        try {
            routesResponse = this.objectMapper.readValue(response, typeReference);
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(ROUTES_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaBustimeResponse<CtaRoute> bustimeResponse = routesResponse.bustimeResponse();

        List<CtaError> errors = bustimeResponse.error();
        List<CtaRoute> routes = bustimeResponse.data();

        if ((errors == null) && (routes == null)) {
            throw new Cta4jException("Invalid response from %s".formatted(ROUTES_ENDPOINT));
        }

        if ((errors != null) && !errors.isEmpty()) {
            String message = this.buildErrorMessage(ROUTES_ENDPOINT, errors);

            throw new Cta4jException("Error response from %s: %s".formatted(ROUTES_ENDPOINT, message));
        }

        if ((routes == null) || routes.isEmpty()) {
            return List.of();
        }

        return routes.stream()
                     .map(this.routeMapper::toDomain)
                     .toList();
    }

    @Override
    public List<String> getDirections(String routeId) {
        if (routeId == null) {
            throw new IllegalArgumentException("routeId must not be null");
        }

        String url = new URIBuilder()
            .setScheme("https")
            .setHost(this.host)
            .setPath(DIRECTIONS_ENDPOINT)
            .addParameter("rt", routeId)
            .addParameter("key", this.apiKey)
            .addParameter("format", "json")
            .toString();

        String response = HttpUtils.get(url);

        TypeReference<CtaResponse<CtaDirection>> typeReference = new TypeReference<>() {};
        CtaResponse<CtaDirection> directionsResponse;

        try {
            directionsResponse = this.objectMapper.readValue(response, typeReference);
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(DIRECTIONS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaBustimeResponse<CtaDirection> bustimeResponse = directionsResponse.bustimeResponse();

        List<CtaError> errors = bustimeResponse.error();
        List<CtaDirection> directions = bustimeResponse.data();

        if ((errors == null) && (directions == null)) {
            throw new Cta4jException("Invalid response from %s".formatted(DIRECTIONS_ENDPOINT));
        }

        if ((errors != null) && !errors.isEmpty()) {
            String message = this.buildErrorMessage(DIRECTIONS_ENDPOINT, errors);

            throw new Cta4jException("Error response from %s: %s".formatted(DIRECTIONS_ENDPOINT, message));
        }

        if ((directions == null) || directions.isEmpty()) {
            return List.of();
        }

        return directions.stream()
                         .map(CtaDirection::id)
                         .toList();
    }

    @Override
    public List<Stop> getStops(String routeId, String direction) {
        if (routeId == null) {
            throw new IllegalArgumentException("routeId must not be null");
        }

        if (direction == null) {
            throw new IllegalArgumentException("direction must not be null");
        }

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

        TypeReference<CtaResponse<CtaStop>> typeReference = new TypeReference<>() {};
        CtaResponse<CtaStop> stopsResponse;

        try {
            stopsResponse = this.objectMapper.readValue(response, typeReference);
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(STOPS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaBustimeResponse<CtaStop> bustimeResponse = stopsResponse.bustimeResponse();

        List<CtaError> errors = bustimeResponse.error();
        List<CtaStop> stops = bustimeResponse.data();

        if ((errors == null) && (stops == null)) {
            throw new Cta4jException("Invalid response from %s".formatted(STOPS_ENDPOINT));
        }

        if ((errors != null) && !errors.isEmpty()) {
            String message = this.buildErrorMessage(STOPS_ENDPOINT, errors);

            throw new Cta4jException("Error response from %s: %s".formatted(STOPS_ENDPOINT, message));
        }

        if ((stops == null) || stops.isEmpty()) {
            return List.of();
        }

        return stops.stream()
                    .map(this.stopMapper::toDomain)
                    .toList();
    }

    @Override
    public List<Arrival> getArrivalsByStop(String routeId, String stopId) {
        if (routeId == null) {
            throw new IllegalArgumentException("routeId must not be null");
        }

        if (stopId == null) {
            throw new IllegalArgumentException("stopId must not be null");
        }

        String url = new URIBuilder()
            .setScheme("https")
            .setHost(this.host)
            .setPath(PREDICTIONS_ENDPOINT)
            .addParameter("rt", routeId)
            .addParameter("stpid", stopId)
            .addParameter("key", this.apiKey)
            .addParameter("format", "json")
            .toString();

        return this.getArrivals(url);
    }

    @Override
    public List<Arrival> getArrivalsByBus(String busId) {
        if (busId == null) {
            throw new IllegalArgumentException("busId must not be null");
        }

        String url = new URIBuilder()
            .setScheme("https")
            .setHost(this.host)
            .setPath(PREDICTIONS_ENDPOINT)
            .addParameter("vid", busId)
            .addParameter("key", this.apiKey)
            .addParameter("format", "json")
            .toString();

        return this.getArrivals(url);
    }

    @Override
    public List<Detour> getDetours(String routeId, String direction) {
        if (routeId == null) {
            throw new IllegalArgumentException("routeId must not be null");
        }

        if (direction == null) {
            throw new IllegalArgumentException("direction must not be null");
        }

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

        TypeReference<CtaResponse<CtaDetour>> typeReference = new TypeReference<>() {};
        CtaResponse<CtaDetour> detoursResponse;

        try {
            detoursResponse = this.objectMapper.readValue(response, typeReference);
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(DETOURS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaBustimeResponse<CtaDetour> bustimeResponse = detoursResponse.bustimeResponse();

        List<CtaError> errors = bustimeResponse.error();
        List<CtaDetour> detours = bustimeResponse.data();

        if ((errors == null) && (detours == null)) {
            throw new Cta4jException("Invalid response from %s".formatted(DETOURS_ENDPOINT));
        }

        if ((errors != null) && !errors.isEmpty()) {
            String message = this.buildErrorMessage(DETOURS_ENDPOINT, errors);

            throw new Cta4jException("Error response from %s: %s".formatted(DETOURS_ENDPOINT, message));
        }

        if ((detours == null) || detours.isEmpty()) {
            return List.of();
        }

        return detours.stream()
                   .map(this.detourMapper::toDomain)
                   .toList();
    }

    @Override
    public Optional<Bus> getBus(String id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }

        String url = new URIBuilder()
            .setScheme("https")
            .setHost(this.host)
            .setPath(VEHICLES_ENDPOINT)
            .addParameter("vid", id)
            .addParameter("key", this.apiKey)
            .addParameter("format", "json")
            .toString();

        String response = HttpUtils.get(url);

        TypeReference<CtaResponse<CtaVehicle>> typeReference = new TypeReference<>() {};
        CtaResponse<CtaVehicle> vehicleResponse;

        try {
            vehicleResponse = this.objectMapper.readValue(response, typeReference);
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(VEHICLES_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaBustimeResponse<CtaVehicle> bustimeResponse = vehicleResponse.bustimeResponse();

        List<CtaError> errors = bustimeResponse.error();
        List<CtaVehicle> vehicles = bustimeResponse.data();

        if ((errors == null) && (vehicleResponse == null)) {
            throw new Cta4jException("Invalid response from %s".formatted(VEHICLES_ENDPOINT));
        }

        if ((errors != null) && !errors.isEmpty()) {
            String message = this.buildErrorMessage(VEHICLES_ENDPOINT, errors);

            throw new Cta4jException("Error response from %s: %s".formatted(VEHICLES_ENDPOINT, message));
        }

        if ((vehicles == null) || vehicles.isEmpty()) {
            return Optional.empty();
        }

        if (vehicles.size() > 1) {
            String message = "Multiple buses found for ID %s".formatted(id);

            throw new Cta4jException(message);
        }

        CtaVehicle vehicle = vehicles.getFirst();

        Bus bus = this.busMapper.toDomain(vehicle);

        return Optional.of(bus);
    }

    private String buildErrorMessage(String endpoint, List<CtaError> errors) {
        String message = errors.stream()
                               .map(CtaError::msg)
                               .reduce("%s; %s"::formatted)
                               .orElse("Unknown error");

        return "Error response from %s: %s".formatted(endpoint, message);
    }

    private List<Arrival> getArrivals(String url) {
        String response = HttpUtils.get(url);

        TypeReference<CtaResponse<CtaPrediction>> typeReference = new TypeReference<>() {};
        CtaResponse<CtaPrediction> predictionsResponse;

        try {
            predictionsResponse = this.objectMapper.readValue(response, typeReference);
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(PREDICTIONS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaBustimeResponse<CtaPrediction> bustimeResponse = predictionsResponse.bustimeResponse();

        List<CtaError> errors = bustimeResponse.error();
        List<CtaPrediction> predictions = bustimeResponse.data();

        if ((errors == null) && (predictions == null)) {
            throw new Cta4jException("Invalid response from %s".formatted(PREDICTIONS_ENDPOINT));
        }

        if ((errors != null) && !errors.isEmpty()) {
            String message = this.buildErrorMessage(PREDICTIONS_ENDPOINT, errors);

            throw new Cta4jException("Error response from %s: %s".formatted(PREDICTIONS_ENDPOINT, message));
        }

        if ((predictions == null) || predictions.isEmpty()) {
            return List.of();
        }

        return predictions.stream()
                          .map(this.arrivalMapper::toDomain)
                          .toList();
    }

    public static final class BuilderImpl implements BusClient.Builder {
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
        public BusClient build() {
            String finalHost = (this.host == null) ? DEFAULT_HOST : this.host;

            if (this.apiKey == null) {
                throw new IllegalStateException("API key must not be null");
            }

            return new BusClientImpl(finalHost, this.apiKey);
        }
    }
}
