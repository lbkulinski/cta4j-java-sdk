package com.cta4j.bus.prediction;

import com.cta4j.bus.prediction.model.Prediction;
import com.cta4j.bus.prediction.query.StopsPredictionsQuery;
import com.cta4j.bus.prediction.query.VehiclesPredictionsQuery;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Objects;

/**
 * Provides access to prediction-related endpoints of the CTA BusTime API.
 * <p>
 * This API allows retrieval of predictions by stop IDs or vehicle IDs.
 */
@NullMarked
public interface PredictionsApi {
    /**
     * Retrieves predictions by stop IDs.
     *
     * @param query the query parameters for fetching predictions by stop IDs
     * @return a {@link List} of {@link Prediction}s corresponding to the provided stop IDs, or an empty {@link List}
     * if no predictions are found
     * @throws NullPointerException if {@code query} is {@code null}
     */
    List<Prediction> findByStopIds(StopsPredictionsQuery query);

    /**
     * Retrieves predictions by vehicle IDs.
     *
     * @param query the query parameters for fetching predictions by vehicle IDs
     * @return a {@link List} of {@link Prediction}s corresponding to the provided vehicle IDs, or an empty
     * {@link List} if no predictions are found
     * @throws NullPointerException if {@code query} is {@code null}
     */
    List<Prediction> findByVehicleIds(VehiclesPredictionsQuery query);

    /**
     * Retrieves predictions by route ID and stop ID.
     *
     * @param routeId the route ID
     * @param stopId the stop ID
     * @return a {@link List} of {@link Prediction}s corresponding to the provided route ID and stop ID, or an empty
     * {@link List} if no predictions are found
     * @throws NullPointerException if {@code routeId} or {@code stopId} is {@code null}
     */
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

    /**
     * Retrieves predictions by vehicle ID.
     *
     * @param vehicleId the vehicle ID
     * @return a {@link List} of {@link Prediction}s corresponding to the provided vehicle ID, or an empty {@link List}
     * if no predictions are found
     * @throws NullPointerException if {@code vehicleId} is {@code null}
     */
    default List<Prediction> findByVehicleId(String vehicleId) {
        Objects.requireNonNull(vehicleId);

        List<String> vehicleIds = List.of(vehicleId);

        VehiclesPredictionsQuery query = VehiclesPredictionsQuery.builder(vehicleIds)
                                                                 .build();

        return this.findByVehicleIds(query);
    }
}
