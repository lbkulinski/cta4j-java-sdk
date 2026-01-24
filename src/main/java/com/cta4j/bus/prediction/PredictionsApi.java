package com.cta4j.bus.prediction;

import com.cta4j.bus.prediction.model.Prediction;
import com.cta4j.bus.prediction.query.StopsPredictionsQuery;
import com.cta4j.bus.prediction.query.VehiclesPredictionsQuery;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Objects;

@NullMarked
public interface PredictionsApi {
    List<Prediction> findByStopIds(StopsPredictionsQuery query);

    List<Prediction> findByVehicleIds(VehiclesPredictionsQuery query);

    default List<Prediction> findByRouteIdAndStopId(String routeId, String stopId) {
        Objects.requireNonNull(routeId);
        Objects.requireNonNull(stopId);

        List<String> stopIds = List.of(stopId);
        List<String> routeIds = List.of(routeId);

        StopsPredictionsQuery query = StopsPredictionsQuery.builder(stopIds)
                                                           .routeIds(routeIds)
                                                           .build();

        return this.findByStopIds(query);
    }

    default List<Prediction> findByVehicleId(String vehicleId) {
        Objects.requireNonNull(vehicleId);

        List<String> vehicleIds = List.of(vehicleId);

        VehiclesPredictionsQuery query = VehiclesPredictionsQuery.builder(vehicleIds)
                                                                 .build();

        return this.findByVehicleIds(query);
    }
}
