package com.cta4j.bus.api;

import com.cta4j.bus.api.vehicle.VehiclesApi;

import java.time.Instant;

public interface BusApi {
    Instant systemTime();

    VehiclesApi vehicles();

    RoutesApi routes();

    DirectionsApi directions();

    StopsApi stops();

    PatternsApi patterns();

    PredictionsApi predictions();

    LocalesApi locales();

    DetoursApi detours();
}
