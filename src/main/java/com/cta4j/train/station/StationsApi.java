package com.cta4j.train.station;

import com.cta4j.train.station.model.Station;
import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * Provides access to station-related endpoints.
 * <p>
 * This API allows retrieval of station information, including station names, IDs, and other details.
 * <p>
 * <b>NOTE:</b> The CTA Train Tracker API does not provide an endpoint for retrieving station information. This API
 * uses the City of Chicago's Data Portal as its data source by default. The URL used to retrieve station information
 * is configurable to accommodate changes to the data source.
 */
@NullMarked
public interface StationsApi {
    /**
     * Retrieves all available stations.
     *
     * @return a {@link List} of all available {@link Station}s
     */
    List<Station> list();
}
