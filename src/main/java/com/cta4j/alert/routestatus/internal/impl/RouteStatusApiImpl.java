package com.cta4j.alert.routestatus.internal.impl;

import com.cta4j.alert.common.internal.config.AlertApiConfig;
import com.cta4j.alert.common.internal.util.AlertApiConstants;
import com.cta4j.alert.routestatus.RouteStatusApi;
import com.cta4j.alert.routestatus.exception.Cta4jRouteStatusException;
import com.cta4j.alert.routestatus.exception.RouteStatusErrorCode;
import com.cta4j.alert.routestatus.internal.mapper.RouteStatusMapper;
import com.cta4j.alert.routestatus.internal.wire.CtaRouteInfo;
import com.cta4j.alert.routestatus.internal.wire.CtaRouteStatusResponse;
import com.cta4j.alert.routestatus.internal.wire.CtaRoutes;
import com.cta4j.alert.routestatus.model.RouteStatus;
import com.cta4j.alert.common.model.ServiceType;
import com.cta4j.common.train.TrainLine;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApiStatus.Internal
@NullMarked
public final class RouteStatusApiImpl implements RouteStatusApi {
    private static final Logger log = LoggerFactory.getLogger(RouteStatusApiImpl.class);

    private final AlertApiConfig config;

    public RouteStatusApiImpl(AlertApiConfig config) {
        this.config = Objects.requireNonNull(config);
    }

    @Override
    public List<RouteStatus> list() {
        String url = new URIBuilder()
            .setScheme(this.config.scheme())
            .setHost(this.config.host())
            .setPort(this.config.port())
            .setPath(AlertApiConstants.ROUTE_STATUS_ENDPOINT)
            .addParameter("outputType", "JSON")
            .toString();

        return this.makeRequest(url);
    }

    @Override
    public List<RouteStatus> findByTypes(Collection<ServiceType> types) {
        Objects.requireNonNull(types);

        List<ServiceType> typesList = List.copyOf(types);

        if (typesList.isEmpty()) {
            return List.of();
        }

        String typesString = typesList.stream()
                                      .map(ServiceType::name)
                                      .map(String::toLowerCase)
                                      .collect(Collectors.joining(","));

        String url = new URIBuilder()
            .setScheme(this.config.scheme())
            .setHost(this.config.host())
            .setPort(this.config.port())
            .setPath(AlertApiConstants.ROUTE_STATUS_ENDPOINT)
            .addParameter("type", typesString)
            .addParameter("outputType", "JSON")
            .toString();

        return this.makeRequest(url);
    }

    @Override
    public List<RouteStatus> findByBusRouteIds(Collection<String> routeIds) {
        Objects.requireNonNull(routeIds);

        List<String> routeIdsList = List.copyOf(routeIds);

        if (routeIdsList.isEmpty()) {
            return List.of();
        }

        for (String routeId : routeIdsList) {
            if (isTrainLine(routeId)) {
                String message = """
                %s is a train line, not a bus route; \
                use findByLines(Collection<TrainLine>) instead""".formatted(routeId);

                throw new IllegalArgumentException(message);
            }
        }

        String routeIdsString = String.join(",", routeIdsList);

        String url = new URIBuilder()
            .setScheme(this.config.scheme())
            .setHost(this.config.host())
            .setPort(this.config.port())
            .setPath(AlertApiConstants.ROUTE_STATUS_ENDPOINT)
            .addParameter("routeid", routeIdsString)
            .addParameter("outputType", "JSON")
            .toString();

        return this.makeRequest(url);
    }

    @Override
    public List<RouteStatus> findByLines(Collection<TrainLine> lines) {
        Objects.requireNonNull(lines);

        List<TrainLine> linesList = List.copyOf(lines);

        if (linesList.isEmpty()) {
            return List.of();
        }

        String linesString = linesList.stream()
                                      .map(TrainLine::getCode)
                                      .collect(Collectors.joining(","));

        String url = new URIBuilder()
            .setScheme(this.config.scheme())
            .setHost(this.config.host())
            .setPort(this.config.port())
            .setPath(AlertApiConstants.ROUTE_STATUS_ENDPOINT)
            .addParameter("routeid", linesString)
            .addParameter("outputType", "JSON")
            .toString();

        return this.makeRequest(url);
    }

    @Override
    public List<RouteStatus> findByStationId(String stationId) {
        Objects.requireNonNull(stationId);

        String url = new URIBuilder()
            .setScheme(this.config.scheme())
            .setHost(this.config.host())
            .setPort(this.config.port())
            .setPath(AlertApiConstants.ROUTE_STATUS_ENDPOINT)
            .addParameter("stationid", stationId)
            .addParameter("outputType", "JSON")
            .toString();

        return this.makeRequest(url);
    }

    private List<RouteStatus> makeRequest(String url) {
        String response;

        try {
            response = Request.get(url)
                              .execute()
                              .returnContent()
                              .asString();
        } catch (IOException e) {
            String message = Objects.requireNonNullElse(e.getMessage(), "Request failed");

            throw new Cta4jRouteStatusException(message, e);
        }

        CtaRouteStatusResponse routeStatusResponse;

        try {
            routeStatusResponse = JsonMapper.shared()
                                            .readValue(response, CtaRouteStatusResponse.class);
        } catch (JacksonException e) {
            throw new Cta4jRouteStatusException("Failed to parse response", e);
        }

        CtaRoutes ctaRoutes = routeStatusResponse.ctaRoutes();

        List<CtaRouteInfo> routeInfo = ctaRoutes.routeInfo();

        if (routeInfo != null && !routeInfo.isEmpty()) {
            return routeInfo.stream()
                            .map(RouteStatusMapper.INSTANCE::toDomain)
                            .toList();
        }

        List<String> errorCodeStrings = ctaRoutes.errorCode();

        if (errorCodeStrings == null || errorCodeStrings.isEmpty()) {
            log.warn("Received empty response from {}", AlertApiConstants.ROUTE_STATUS_ENDPOINT);

            return List.of();
        }

        long distinctCount = errorCodeStrings.stream()
                                             .distinct()
                                             .count();

        if (distinctCount > 1L) {
            log.warn(
                "Received multiple distinct error codes from {}: {}",
                AlertApiConstants.ROUTE_STATUS_ENDPOINT,
                errorCodeStrings
            );
        }

        String errorCodeString = errorCodeStrings.getFirst();

        int integerCode;

        try {
            integerCode = Integer.parseInt(errorCodeString);
        } catch (NumberFormatException e) {
            throw new Cta4jRouteStatusException("Failed to parse error code", e);
        }

        RouteStatusErrorCode errorCode = RouteStatusErrorCode.fromCode(integerCode);

        if (errorCode == RouteStatusErrorCode.OK || errorCode == RouteStatusErrorCode.NO_RESULTS) {
            return List.of();
        }

        List<@Nullable String> errorMessages = ctaRoutes.errorMessage();

        String message = errorMessages == null || errorMessages.isEmpty()
            ? "An unknown error occurred."
            : errorMessages.getFirst();

        if (message == null || message.isBlank()) {
            message = "An unknown error occurred.";
        }

        throw new Cta4jRouteStatusException(message, integerCode);
    }

    private static boolean isTrainLine(String routeId) {
        return Arrays.stream(TrainLine.values())
                     .map(TrainLine::getCode)
                     .anyMatch(code -> code.equalsIgnoreCase(routeId));
    }
}
