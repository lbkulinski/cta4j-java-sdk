package com.cta4j.client;

import com.cta4j.external.bus.detour.CtaDetoursResponse;
import com.cta4j.external.bus.prediction.CtaPredictionsResponse;
import com.cta4j.external.bus.vehicle.CtaVehicleResponse;

public interface CtaBusClient {
//    @RequestLine("GET /bustime/api/v3/getpredictions?rt={routeId}&stpid={stopId}")
    CtaPredictionsResponse getPredictions(String routeId, int stopId);
//
//    @RequestLine("GET /bustime/api/v3/getdetours?rt={routeId}&rtdir={direction}")
    CtaDetoursResponse getDetours(String routeId, String direction);
//
//    @RequestLine("GET /bustime/api/v3/getvehicles?vid={id}")
    CtaVehicleResponse getVehicle(String id);
}
