package com.cta4j.bus.api.vehicle.impl;

import com.cta4j.bus.api.ApiUtils;
import com.cta4j.bus.api.vehicle.VehiclesApi;
import com.cta4j.bus.api.vehicle.external.CtaVehicle;
import com.cta4j.bus.api.vehicle.mapper.VehicleMapper;
import com.cta4j.bus.api.vehicle.model.Vehicle;
import com.cta4j.bus.external.CtaBustimeResponse;
import com.cta4j.bus.external.CtaError;
import com.cta4j.bus.external.CtaResponse;
import com.cta4j.exception.Cta4jException;
import com.cta4j.util.HttpUtils;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.mapstruct.factory.Mappers;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.Collection;
import java.util.List;

@NullMarked
@ApiStatus.Internal
public final class VehiclesApiImpl implements VehiclesApi {
    private static final String VEHICLES_ENDPOINT = String.format("%s/getvehicles", ApiUtils.API_PREFIX);

    private final String host;
    private final String apiKey;
    private final ObjectMapper objectMapper;
    private final VehicleMapper vehicleMapper;

    public VehiclesApiImpl(
        @Nullable String host,
        @Nullable String apiKey,
        @Nullable ObjectMapper objectMapper
    ) {
        if (host == null) {
            throw new IllegalArgumentException("host must not be null");
        }

        if (apiKey == null) {
            throw new IllegalArgumentException("apiKey must not be null");
        }

        if (objectMapper == null) {
            throw new IllegalArgumentException("objectMapper must not be null");
        }

        this.host = host;
        this.apiKey = apiKey;
        this.objectMapper = objectMapper;
        this.vehicleMapper = Mappers.getMapper(VehicleMapper.class);
    }

    @Override
    public List<Vehicle> findByIds(@Nullable Collection<@Nullable String> ids) {
        if (ids == null) {
            throw new IllegalArgumentException("ids must not be null");
        }

        if (ids.isEmpty()) {
            return List.of();
        }

        for (String id : ids) {
            if (id == null) {
                throw new IllegalArgumentException("ids must not contain null elements");
            }
        }

        String idsString = String.join(",", ids);

        String url = new URIBuilder()
            .setScheme(ApiUtils.SCHEME)
            .setHost(this.host)
            .setPath(VEHICLES_ENDPOINT)
            .addParameter("vid", idsString)
            .addParameter("tmres", "s")
            .addParameter("key", this.apiKey)
            .addParameter("format", "json")
            .toString();

        return this.makeRequest(url);
    }

    @Override
    public List<Vehicle> findByRouteIds(@Nullable Collection<@Nullable String> routeIds) {
        if (routeIds == null) {
            throw new IllegalArgumentException("routeIds must not be null");
        }

        if (routeIds.isEmpty()) {
            return List.of();
        }

        for (String routeId : routeIds) {
            if (routeId == null) {
                throw new IllegalArgumentException("routeIds must not contain null elements");
            }
        }

        String routeIdsString = String.join(",", routeIds);

        String url = new URIBuilder()
            .setScheme(ApiUtils.SCHEME)
            .setHost(this.host)
            .setPath(VEHICLES_ENDPOINT)
            .addParameter("rt", routeIdsString)
            .addParameter("tmres", "s")
            .addParameter("key", this.apiKey)
            .addParameter("format", "json")
            .toString();

        return this.makeRequest(url);
    }

    private List<Vehicle> makeRequest(String url) {
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

        if ((errors != null) && !errors.isEmpty()) {
            String message = ApiUtils.buildErrorMessage(VEHICLES_ENDPOINT, errors);

            throw new Cta4jException(message);
        }

        if ((vehicles == null) || vehicles.isEmpty()) {
            return List.of();
        }

        return vehicles.stream()
                       .map(this.vehicleMapper::toDomain)
                       .toList();
    }
}
