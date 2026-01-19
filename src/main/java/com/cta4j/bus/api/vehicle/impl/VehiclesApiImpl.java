package com.cta4j.bus.api.vehicle.impl;

import com.cta4j.bus.api.common.util.ApiUtils;
import com.cta4j.bus.api.vehicle.VehiclesApi;
import com.cta4j.bus.api.vehicle.external.CtaVehicle;
import com.cta4j.bus.api.vehicle.mapper.VehicleMapper;
import com.cta4j.bus.api.vehicle.model.Vehicle;
import com.cta4j.bus.api.common.external.CtaBustimeResponse;
import com.cta4j.bus.api.common.external.CtaError;
import com.cta4j.bus.api.common.external.CtaResponse;
import com.cta4j.common.exception.Cta4jException;
import com.cta4j.common.util.HttpUtils;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@NullMarked
@ApiStatus.Internal
public final class VehiclesApiImpl implements VehiclesApi {
    private static final String VEHICLES_ENDPOINT = String.format("%s/getvehicles", ApiUtils.API_PREFIX);

    private final String host;
    private final String apiKey;
    private final ObjectMapper objectMapper;

    public VehiclesApiImpl(
        String host,
        String apiKey,
        ObjectMapper objectMapper
    ) {
        this.host = Objects.requireNonNull(host);
        this.apiKey = Objects.requireNonNull(apiKey);
        this.objectMapper = Objects.requireNonNull(objectMapper);
    }

    @Override
    public List<Vehicle> findByIds(Collection<String> ids) {
        Objects.requireNonNull(ids);

        ids.forEach(Objects::requireNonNull);

        if (ids.isEmpty()) {
            return List.of();
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
    public List<Vehicle> findByRouteIds(Collection<String> routeIds) {
        Objects.requireNonNull(routeIds);

        routeIds.forEach(Objects::requireNonNull);

        if (routeIds.isEmpty()) {
            return List.of();
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
                       .map(VehicleMapper.MAPPER::toDomain)
                       .toList();
    }
}
