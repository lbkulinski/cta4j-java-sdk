package com.cta4j.bus.prediction;

import com.cta4j.bus.common.exception.Cta4jBusException;
import com.cta4j.bus.prediction.model.Prediction;
import com.cta4j.bus.prediction.query.StopPredictionsQuery;
import com.cta4j.bus.prediction.query.VehiclePredictionsQuery;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/// Provides access to prediction-related endpoints of the CTA Bus Tracker API.
///
/// This API allows retrieval of predictions by stop IDs or vehicle IDs.
@NullMarked
public interface PredictionsApi {
    /// Retrieves predictions by stop IDs.
    ///
    /// @param query the query parameters for fetching predictions by stop IDs
    /// @return a [List] of [Prediction]s corresponding to the provided stop IDs, or an empty [List] if no predictions
    /// are found
    /// @throws NullPointerException if `query` is `null`
    /// @throws Cta4jBusException if the API returns an error response or the response cannot be parsed
    List<Prediction> findByStopIds(StopPredictionsQuery query);

    /// Retrieves predictions by stop IDs.
    ///
    /// @param stopIds a [Collection] of stop IDs
    /// @return a [List] of [Prediction]s corresponding to the provided stop IDs, or an empty [List] if no predictions
    /// are found
    /// @throws NullPointerException if `stopIds` is `null`, or if any element of `stopIds` is `null`
    /// @throws Cta4jBusException if the API returns an error response or the response cannot be parsed
    default List<Prediction> findByStopIds(Collection<String> stopIds) {
        Objects.requireNonNull(stopIds);

        List<String> stopIdsList = List.copyOf(stopIds);

        StopPredictionsQuery query = StopPredictionsQuery.builder(stopIdsList)
                                                         .build();

        return this.findByStopIds(query);
    }

    /// Retrieves predictions by stop ID.
    ///
    /// @param stopId the stop ID
    /// @return a [List] of [Prediction]s corresponding to the provided stop ID, or an empty [List] if no predictions
    /// are found
    /// @throws NullPointerException if `stopId` is `null`
    /// @throws Cta4jBusException if the API returns an error response or the response cannot be parsed
    default List<Prediction> findByStopId(String stopId) {
        Objects.requireNonNull(stopId);

        List<String> stopIds = List.of(stopId);

        StopPredictionsQuery query = StopPredictionsQuery.builder(stopIds)
                                                         .build();

        return this.findByStopIds(query);
    }

    /// Retrieves predictions by vehicle IDs.
    ///
    /// @param query the query parameters for fetching predictions by vehicle IDs
    /// @return a [List] of [Prediction]s corresponding to the provided vehicle IDs, or an empty [List] if no
    /// predictions are found
    /// @throws NullPointerException if `query` is `null`
    /// @throws Cta4jBusException if the API returns an error response or the response cannot be parsed
    List<Prediction> findByVehicleIds(VehiclePredictionsQuery query);

    /// Retrieves predictions by vehicle IDs.
    ///
    /// @param vehicleIds a [Collection] of vehicle IDs
    /// @return a [List] of [Prediction]s corresponding to the provided vehicle IDs, or an empty [List] if no
    /// predictions are found
    /// @throws NullPointerException if `vehicleIds` is `null`, or if any element of `vehicleIds` is `null`
    /// @throws Cta4jBusException if the API returns an error response or the response cannot be parsed
    default List<Prediction> findByVehicleIds(Collection<String> vehicleIds) {
        Objects.requireNonNull(vehicleIds);

        List<String> vehicleIdsList = List.copyOf(vehicleIds);

        VehiclePredictionsQuery query = VehiclePredictionsQuery.builder(vehicleIdsList)
                                                               .build();

        return this.findByVehicleIds(query);
    }

    /// Retrieves predictions by vehicle ID.
    ///
    /// @param vehicleId the vehicle ID
    /// @return a [List] of [Prediction]s corresponding to the provided vehicle ID, or an empty [List] if no
    /// predictions are found
    /// @throws NullPointerException if `vehicleId` is `null`
    /// @throws Cta4jBusException if the API returns an error response or the response cannot be parsed
    default List<Prediction> findByVehicleId(String vehicleId) {
        Objects.requireNonNull(vehicleId);

        List<String> vehicleIds = List.of(vehicleId);

        VehiclePredictionsQuery query = VehiclePredictionsQuery.builder(vehicleIds)
                                                               .build();

        return this.findByVehicleIds(query);
    }

    /// Retrieves predictions by route ID and stop ID.
    ///
    /// @param routeId the route ID
    /// @param stopId the stop ID
    /// @return a [List] of [Prediction]s corresponding to the provided route ID and stop ID, or an empty [List] if no
    /// predictions are found
    /// @throws NullPointerException if `routeId` or `stopId` is `null`
    /// @throws Cta4jBusException if the API returns an error response or the response cannot be parsed
    default List<Prediction> findByRouteIdAndStopId(String routeId, String stopId) {
        Objects.requireNonNull(routeId);
        Objects.requireNonNull(stopId);

        List<String> stopIds = List.of(stopId);
        List<String> routeIds = List.of(routeId);

        StopPredictionsQuery query = StopPredictionsQuery.builder(stopIds)
                                                         .routeIds(routeIds)
                                                         .build();

        return this.findByStopIds(query);
    }
}
