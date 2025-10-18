package com.cta4j.train.model;

import java.util.List;

/**
 * A train currently in service.
 *
 * @param coordinates the coordinates and heading of the train
 * @param arrivals the list of upcoming train arrivals for the train
 */
public record Train(
    TrainCoordinates coordinates,

    List<UpcomingTrainArrival> arrivals
) {
}
