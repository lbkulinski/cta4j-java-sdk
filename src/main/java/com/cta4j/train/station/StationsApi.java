package com.cta4j.train.station;

import com.cta4j.train.station.model.Station;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public interface StationsApi {
    List<Station> list();
}
