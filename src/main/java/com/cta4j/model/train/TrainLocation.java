package com.cta4j.model.train;

import java.util.List;

public record TrainLocation(
    TrainCoordinates coordinates,

    List<UpcomingTrainArrival> arrivals
) {
}
