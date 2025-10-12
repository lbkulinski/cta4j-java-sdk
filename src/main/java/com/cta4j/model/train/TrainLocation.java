package com.cta4j.model.train;

import java.util.List;

/**
 * The location of a train along with its upcoming arrivals.
 *
 * @param coordinates the coordinates and heading of the train
 * @param arrivals the list of upcoming train arrivals for the train
 */
public record TrainLocation(
    TrainCoordinates coordinates,

    List<UpcomingTrainArrival> arrivals
) {
}
