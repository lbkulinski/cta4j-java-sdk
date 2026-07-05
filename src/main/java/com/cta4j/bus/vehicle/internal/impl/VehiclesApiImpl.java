package com.cta4j.bus.vehicle.internal.impl;

import com.cta4j.bus.common.exception.Cta4jBusException;
import com.cta4j.bus.common.internal.config.BusApiConfig;
import com.cta4j.bus.common.internal.util.ApiUtils;
import com.cta4j.bus.common.internal.util.BusApiConstants;
import com.cta4j.bus.common.internal.wire.CtaResponse;
import com.cta4j.bus.vehicle.VehiclesApi;
import com.cta4j.bus.vehicle.internal.mapper.VehicleMapper;
import com.cta4j.bus.vehicle.internal.wire.CtaVehicle;
import com.cta4j.bus.vehicle.internal.wire.CtaVehicleBustimeResponse;
import com.cta4j.bus.vehicle.internal.wire.CtaVehicleError;
import com.cta4j.bus.vehicle.model.Vehicle;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class VehiclesApiImpl implements VehiclesApi {
    private static final TypeReference<CtaResponse<CtaVehicleBustimeResponse>> TYPE_REFERENCE =
        new TypeReference<>() {};

    private final BusApiConfig config;

    public VehiclesApiImpl(BusApiConfig config) {
        this.config = Objects.requireNonNull(config);
    }

    @Override
    public List<Vehicle> findByIds(Collection<String> ids) {
        Objects.requireNonNull(ids);

        ids = List.copyOf(ids);

        if (ids.isEmpty()) {
            return List.of();
        }

        ApiUtils.requireMaxIds(ids, "vehicle");

        String idsString = String.join(",", ids);

        String url = new URIBuilder()
            .setScheme(this.config.scheme())
            .setHost(this.config.host())
            .setPort(this.config.port())
            .setPath(BusApiConstants.VEHICLES_ENDPOINT)
            .addParameter("vid", idsString)
            .addParameter("tmres", "s")
            .addParameter("key", this.config.apiKey())
            .addParameter("format", "json")
            .toString();

        return this.makeRequest(url);
    }

    @Override
    public List<Vehicle> findByRouteIds(Collection<String> routeIds) {
        Objects.requireNonNull(routeIds);

        routeIds = List.copyOf(routeIds);

        if (routeIds.isEmpty()) {
            return List.of();
        }

        ApiUtils.requireMaxIds(routeIds, "route");

        String routeIdsString = String.join(",", routeIds);

        String url = new URIBuilder()
            .setScheme(this.config.scheme())
            .setHost(this.config.host())
            .setPort(this.config.port())
            .setPath(BusApiConstants.VEHICLES_ENDPOINT)
            .addParameter("rt", routeIdsString)
            .addParameter("tmres", "s")
            .addParameter("key", this.config.apiKey())
            .addParameter("format", "json")
            .toString();

        return this.makeRequest(url);
    }

    private List<Vehicle> makeRequest(String url) {
        String response;

        try {
            response = Request.get(url)
                              .execute()
                              .returnContent()
                              .asString();
        } catch (IOException e) {
            String message = e.getMessage();

            throw new Cta4jBusException(message, BusApiConstants.VEHICLES_ENDPOINT, e);
        }

        CtaResponse<CtaVehicleBustimeResponse> vehicleResponse;

        try {
            vehicleResponse = JsonMapper.shared()
                                        .readValue(response, TYPE_REFERENCE);
        } catch (JacksonException e) {
            throw new Cta4jBusException("Failed to parse response", BusApiConstants.VEHICLES_ENDPOINT, e);
        }

        CtaVehicleBustimeResponse bustimeResponse = vehicleResponse.bustimeResponse();

        List<CtaVehicle> vehicles = bustimeResponse.vehicle();
        List<CtaVehicleError> errors = bustimeResponse.error();

        if (vehicles != null && !vehicles.isEmpty()) {
            return vehicles.stream()
                           .map(VehicleMapper.INSTANCE::toDomain)
                           .toList();
        }

        ApiUtils.checkErrors(errors, BusApiConstants.VEHICLES_ENDPOINT);

        return List.of();
    }
}
