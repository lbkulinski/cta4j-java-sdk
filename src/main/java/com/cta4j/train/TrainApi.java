package com.cta4j.train;

import com.cta4j.train.station.StationsApi;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface TrainApi {
    StationsApi stations();
}
