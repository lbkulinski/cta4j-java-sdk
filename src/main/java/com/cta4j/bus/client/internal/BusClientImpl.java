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
import com.cta4j.bus.mapper.util.CtaBusMappingQualifiers;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.ApiStatus;

import java.time.Instant;
import java.util.List;

@NullMarked
@ApiStatus.Internal
@SuppressWarnings("ConstantConditions")
public final class BusClientImpl implements BusClient {
    private static final Logger log = LoggerFactory.getLogger(BusClientImpl.class);

    private static final String SCHEME = "https";
    private static final String DEFAULT_HOST = "ctabustracker.com";
    private static final String API_PREFIX = "/bustime/api/v3";
    private static final String SYSTEM_TIME_ENDPOINT = String.format("%s/gettime", API_PREFIX);
    private static final String ROUTES_ENDPOINT = String.format("%s/getroutes", API_PREFIX);
    private static final String DIRECTIONS_ENDPOINT = String.format("%s/getdirections", API_PREFIX);
    private static final String STOPS_ENDPOINT = String.format("%s/getstops", API_PREFIX);
    private static final String PREDICTIONS_ENDPOINT = String.format("%s/getpredictions", API_PREFIX);
    private static final String DETOURS_ENDPOINT = String.format("%s/getdetours", API_PREFIX);
    private static final String VEHICLES_ENDPOINT = String.format("%s/getvehicles", API_PREFIX);

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
    public Instant getSystemTime() {
        String url = new URIBuilder()
            .setScheme(SCHEME)
            .setHost(this.host)
            .setPath(SYSTEM_TIME_ENDPOINT)
            .addParameter("key", this.apiKey)
            .addParameter("format", "json")
            .toString();

        String response = HttpUtils.get(url);

        TypeReference<CtaResponse<String>> typeReference = new TypeReference<>() {};
        CtaResponse<String> timeResponse;

        try {
            timeResponse = this.objectMapper.readValue(response, typeReference);
        } catch (JacksonException e) {
            String message = String.format("Failed to parse response from %s", SYSTEM_TIME_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaBustimeResponse<String> bustimeResponse = timeResponse.bustimeResponse();

        List<CtaError> errors = bustimeResponse.error();
        String systemTime = bustimeResponse.data();

        if ((errors == null) && (systemTime == null)) {
            String message = String.format(
                "System time bustime response missing both error and data from %s",
                SYSTEM_TIME_ENDPOINT
            );

            throw new Cta4jException(message);
        }

        if ((errors != null) && !errors.isEmpty()) {
            String message = this.buildErrorMessage(SYSTEM_TIME_ENDPOINT, errors);

            throw new Cta4jException(message);
        }

        if (systemTime == null) {
            String message = String.format("No system time data returned from %s", SYSTEM_TIME_ENDPOINT);

            throw new Cta4jException(message);
        }

        return CtaBusMappingQualifiers.mapTimestamp(systemTime);
    }

    @Override
    public List<Bus> findBusesById(Iterable<String> ids) {
        if (ids == null) {
            throw new IllegalArgumentException("ids must not be null");
        }

        for (String id : ids) {
            if (id == null) {
                throw new IllegalArgumentException("ids must not contain null elements");
            }
        }

        String idsString = String.join(",", ids);

        String url = new URIBuilder()
            .setScheme(SCHEME)
            .setHost(this.host)
            .setPath(VEHICLES_ENDPOINT)
            .addParameter("vid", idsString)
            .addParameter("tmres", "s")
            .addParameter("key", this.apiKey)
            .addParameter("format", "json")
            .toString();

        return this.getBuses(url);
    }

    @Override
    public List<Bus> findBusesByRouteId(Iterable<String> routeIds) {
        if (routeIds == null) {
            throw new IllegalArgumentException("routeIds must not be null");
        }

        for (String routeId : routeIds) {
            if (routeId == null) {
                throw new IllegalArgumentException("routeIds must not contain null elements");
            }
        }

        String routeIdsString = String.join(",", routeIds);

        String url = new URIBuilder()
            .setScheme(SCHEME)
            .setHost(this.host)
            .setPath(VEHICLES_ENDPOINT)
            .addParameter("rt", routeIdsString)
            .addParameter("tmres", "s")
            .addParameter("key", this.apiKey)
            .addParameter("format", "json")
            .toString();

        return this.getBuses(url);
    }

    @Override
    public List<Route> getRoutes() {
        String url = new URIBuilder()
            .setScheme(SCHEME)
            .setHost(this.host)
            .setPath(ROUTES_ENDPOINT)
            .addParameter("key", this.apiKey)
            .addParameter("format", "json")
            .toString();

        String response = HttpUtils.get(url);

        TypeReference<CtaResponse<List<CtaRoute>>> typeReference = new TypeReference<>() {};
        CtaResponse<List<CtaRoute>> routesResponse;

        try {
            routesResponse = this.objectMapper.readValue(response, typeReference);
        } catch (JacksonException e) {
            String message = String.format("Failed to parse response from %s", ROUTES_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaBustimeResponse<List<CtaRoute>> bustimeResponse = routesResponse.bustimeResponse();

        List<CtaError> errors = bustimeResponse.error();
        List<CtaRoute> routes = bustimeResponse.data();

        if ((errors == null) && (routes == null)) {
            log.debug("Routes bustime response missing both error and data from {}", ROUTES_ENDPOINT);

            return List.of();
        }

        if ((errors != null) && !errors.isEmpty()) {
            String message = this.buildErrorMessage(ROUTES_ENDPOINT, errors);

            throw new Cta4jException(message);
        }

        if ((routes == null) || routes.isEmpty()) {
            return List.of();
        }

        return routes.stream()
                     .map(this.routeMapper::toDomain)
                     .toList();
    }

    @Override
    public List<String> findDirectionsByRouteId(String routeId) {
        if (routeId == null) {
            throw new IllegalArgumentException("routeId must not be null");
        }

        String url = new URIBuilder()
            .setScheme(SCHEME)
            .setHost(this.host)
            .setPath(DIRECTIONS_ENDPOINT)
            .addParameter("rt", routeId)
            .addParameter("key", this.apiKey)
            .addParameter("format", "json")
            .toString();

        String response = HttpUtils.get(url);

        TypeReference<CtaResponse<List<CtaDirection>>> typeReference = new TypeReference<>() {};
        CtaResponse<List<CtaDirection>> directionsResponse;

        try {
            directionsResponse = this.objectMapper.readValue(response, typeReference);
        } catch (JacksonException e) {
            String message = String.format("Failed to parse response from %s", DIRECTIONS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaBustimeResponse<List<CtaDirection>> bustimeResponse = directionsResponse.bustimeResponse();

        List<CtaError> errors = bustimeResponse.error();
        List<CtaDirection> directions = bustimeResponse.data();

        if ((errors == null) && (directions == null)) {
            log.debug("Directions bustime response missing both error and data from {}", DIRECTIONS_ENDPOINT);

            return List.of();
        }

        if ((errors != null) && !errors.isEmpty()) {
            String message = this.buildErrorMessage(DIRECTIONS_ENDPOINT, errors);

            throw new Cta4jException(message);
        }

        if ((directions == null) || directions.isEmpty()) {
            return List.of();
        }

        return directions.stream()
                         .map(CtaDirection::id)
                         .toList();
    }

    @Override
    public List<Stop> findStopsByRouteIdAndDirection(String routeId, String direction) {
        if (routeId == null) {
            throw new IllegalArgumentException("routeId must not be null");
        }

        if (direction == null) {
            throw new IllegalArgumentException("direction must not be null");
        }

        String url = new URIBuilder()
            .setScheme(SCHEME)
            .setHost(this.host)
            .setPath(STOPS_ENDPOINT)
            .addParameter("rt", routeId)
            .addParameter("dir", direction)
            .addParameter("key", this.apiKey)
            .addParameter("format", "json")
            .toString();

        String response = HttpUtils.get(url);

        TypeReference<CtaResponse<List<CtaStop>>> typeReference = new TypeReference<>() {};
        CtaResponse<List<CtaStop>> stopsResponse;

        try {
            stopsResponse = this.objectMapper.readValue(response, typeReference);
        } catch (JacksonException e) {
            String message = String.format("Failed to parse response from %s", STOPS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaBustimeResponse<List<CtaStop>> bustimeResponse = stopsResponse.bustimeResponse();

        List<CtaError> errors = bustimeResponse.error();
        List<CtaStop> stops = bustimeResponse.data();

        if ((errors == null) && (stops == null)) {
            log.debug("Stops bustime response missing both error and data from {}", STOPS_ENDPOINT);

            return List.of();
        }

        if ((errors != null) && !errors.isEmpty()) {
            String message = this.buildErrorMessage(STOPS_ENDPOINT, errors);

            throw new Cta4jException(message);
        }

        if ((stops == null) || stops.isEmpty()) {
            return List.of();
        }

        return stops.stream()
                    .map(this.stopMapper::toDomain)
                    .toList();
    }

    @Override
    public List<Arrival> findArrivalsByRouteIdAndStopId(String routeId, String stopId) {
        if (routeId == null) {
            throw new IllegalArgumentException("routeId must not be null");
        }

        if (stopId == null) {
            throw new IllegalArgumentException("stopId must not be null");
        }

        String url = new URIBuilder()
            .setScheme(SCHEME)
            .setHost(this.host)
            .setPath(PREDICTIONS_ENDPOINT)
            .addParameter("rt", routeId)
            .addParameter("stpid", stopId)
            .addParameter("tmres", "s")
            .addParameter("key", this.apiKey)
            .addParameter("format", "json")
            .toString();

        return this.getArrivals(url);
    }

    @Override
    public List<Arrival> findArrivalsByBusId(String busId) {
        if (busId == null) {
            throw new IllegalArgumentException("busId must not be null");
        }

        String url = new URIBuilder()
            .setScheme(SCHEME)
            .setHost(this.host)
            .setPath(PREDICTIONS_ENDPOINT)
            .addParameter("vid", busId)
            .addParameter("tmres", "s")
            .addParameter("key", this.apiKey)
            .addParameter("format", "json")
            .toString();

        return this.getArrivals(url);
    }

    @Override
    public List<Detour> findDetoursByRouteIdAndDirection(String routeId, String direction) {
        if (routeId == null) {
            throw new IllegalArgumentException("routeId must not be null");
        }

        if (direction == null) {
            throw new IllegalArgumentException("direction must not be null");
        }

        String url = new URIBuilder()
            .setScheme(SCHEME)
            .setHost(this.host)
            .setPath(DETOURS_ENDPOINT)
            .addParameter("rt", routeId)
            .addParameter("dir", direction)
            .addParameter("tmres", "s") //TODO: Is tmres an option for detours?
            .addParameter("key", this.apiKey)
            .addParameter("format", "json")
            .toString();

        String response = HttpUtils.get(url);

        TypeReference<CtaResponse<List<CtaDetour>>> typeReference = new TypeReference<>() {};
        CtaResponse<List<CtaDetour>> detoursResponse;

        try {
            detoursResponse = this.objectMapper.readValue(response, typeReference);
        } catch (JacksonException e) {
            String message = String.format("Failed to parse response from %s", DETOURS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaBustimeResponse<List<CtaDetour>> bustimeResponse = detoursResponse.bustimeResponse();

        List<CtaError> errors = bustimeResponse.error();
        List<CtaDetour> detours = bustimeResponse.data();

        if ((errors == null) && (detours == null)) {
            log.debug("Detours bustime response missing both error and data from {}", DETOURS_ENDPOINT);

            return List.of();
        }

        if ((errors != null) && !errors.isEmpty()) {
            String message = this.buildErrorMessage(DETOURS_ENDPOINT, errors);

            throw new Cta4jException(message);
        }

        if ((detours == null) || detours.isEmpty()) {
            return List.of();
        }

        return detours.stream()
                   .map(this.detourMapper::toDomain)
                   .toList();
    }

    private String buildErrorMessage(String endpoint, List<CtaError> errors) {
        String message = errors.stream()
                               .map(CtaError::msg)
                               .reduce("%s; %s"::formatted)
                               .orElse("Unknown error");

        return String.format("Error response from %s: %s", endpoint, message);
    }

    private List<Bus> getBuses(String url) {
        String response = HttpUtils.get(url);

        TypeReference<CtaResponse<List<CtaVehicle>>> typeReference = new TypeReference<>() {};
        CtaResponse<List<CtaVehicle>> vehicleResponse;

        try {
            vehicleResponse = this.objectMapper.readValue(response, typeReference);
        } catch (JacksonException e) {
            String message = String.format("Failed to parse response from %s", VEHICLES_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaBustimeResponse<List<CtaVehicle>> bustimeResponse = vehicleResponse.bustimeResponse();

        List<CtaError> errors = bustimeResponse.error();
        List<CtaVehicle> vehicles = bustimeResponse.data();

        if ((errors == null) && (vehicleResponse == null)) {
            log.debug("Vehicles bustime response missing both error and data from {}", VEHICLES_ENDPOINT);

            return List.of();
        }

        if ((errors != null) && !errors.isEmpty()) {
            String message = this.buildErrorMessage(VEHICLES_ENDPOINT, errors);

            throw new Cta4jException(message);
        }

        if ((vehicles == null) || vehicles.isEmpty()) {
            return List.of();
        }

        return vehicles.stream()
                       .map(this.busMapper::toDomain)
                       .toList();
    }

    private List<Arrival> getArrivals(String url) {
        String response = HttpUtils.get(url);

        TypeReference<CtaResponse<List<CtaPrediction>>> typeReference = new TypeReference<>() {};
        CtaResponse<List<CtaPrediction>> predictionsResponse;

        try {
            predictionsResponse = this.objectMapper.readValue(response, typeReference);
        } catch (JacksonException e) {
            String message = String.format("Failed to parse response from %s", PREDICTIONS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaBustimeResponse<List<CtaPrediction>> bustimeResponse = predictionsResponse.bustimeResponse();

        List<CtaError> errors = bustimeResponse.error();
        List<CtaPrediction> predictions = bustimeResponse.data();

        if ((errors == null) && (predictions == null)) {
            log.debug("Predictions bustime response missing both error and data from {}", PREDICTIONS_ENDPOINT);

            return List.of();
        }

        if ((errors != null) && !errors.isEmpty()) {
            String message = this.buildErrorMessage(PREDICTIONS_ENDPOINT, errors);

            throw new Cta4jException(message);
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
