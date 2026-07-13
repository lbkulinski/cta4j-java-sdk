package com.cta4j.alert.routestatus.internal.impl;

import com.cta4j.alert.common.internal.config.AlertApiConfig;
import com.cta4j.alert.routestatus.RouteStatusApi;
import com.cta4j.alert.routestatus.exception.Cta4jRouteStatusException;
import com.cta4j.alert.routestatus.exception.RouteStatusErrorCode;
import com.cta4j.alert.routestatus.internal.mapper.RouteStatusMapper;
import com.cta4j.alert.routestatus.internal.wire.CtaRouteInfo;
import com.cta4j.alert.routestatus.internal.wire.CtaRouteStatusResponse;
import com.cta4j.alert.routestatus.internal.wire.CtaRoutes;
import com.cta4j.alert.routestatus.model.RouteStatus;
import com.cta4j.alert.routestatus.model.ServiceType;
import org.apache.hc.client5.http.fluent.Request;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class RouteStatusApiImpl implements RouteStatusApi {
    private final AlertApiConfig config;

    public RouteStatusApiImpl(AlertApiConfig config) {
        this.config = config;
    }

    @Override
    public List<RouteStatus> list() {
        return List.of();
    }

    @Override
    public List<RouteStatus> findByTypes(Collection<ServiceType> types) {
        Objects.requireNonNull(types);

        List<ServiceType> typesList = List.copyOf(types);

        return List.of();
    }

    @Override
    public List<RouteStatus> findByRouteIds(Collection<String> routeIds) {
        Objects.requireNonNull(routeIds);

        List<String> routeIdsList = List.copyOf(routeIds);

        return List.of();
    }

    @Override
    public List<RouteStatus> findByStationId(String stationId) {
        Objects.requireNonNull(stationId);

        return List.of();
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

        int integerCode;

        try {
            integerCode = Integer.parseInt(ctaRoutes.errorCode());
        } catch (NumberFormatException e) {
            throw new Cta4jRouteStatusException("Failed to parse error code", e);
        }

        RouteStatusErrorCode errorCode = RouteStatusErrorCode.fromCode(integerCode);

        if (errorCode == RouteStatusErrorCode.OK || errorCode == RouteStatusErrorCode.NO_RESULTS) {
            return List.of();
        }

        String errorMessage = ctaRoutes.errorMessage();

        String message = errorMessage == null || errorMessage.isBlank()
            ? "An unknown error occurred."
            : errorMessage;

        throw new Cta4jRouteStatusException(message, integerCode);
    }
}
