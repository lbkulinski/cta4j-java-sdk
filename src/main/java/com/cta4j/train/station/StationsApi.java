package com.cta4j.train.station;

import com.cta4j.train.common.exception.Cta4jTrainException;
import com.cta4j.train.station.internal.impl.StationsApiImpl;
import com.cta4j.train.station.model.Station;
import org.jspecify.annotations.NullMarked;

import java.util.List;

/// Primary entry point for retrieving CTA train station information.
///
/// This API allows retrieval of station information, including station names, IDs, and other details. It requires no
/// API key.
///
/// **NOTE:** The CTA Train Tracker API does not provide an endpoint for retrieving station information. This API uses
/// the City of Chicago's Data Portal as its data source by default. The URL used to retrieve station information is
/// configurable to accommodate changes to the data source.
///
/// Instances of `StationsApi` are immutable and thread-safe once built. Use [#builder()] to construct a configured
/// instance.
@NullMarked
public interface StationsApi {
    /// Retrieves all available stations.
    ///
    /// @return a [List] of all available [Station]s, or an empty [List] if no stations are found
    /// @throws Cta4jTrainException if the API returns an error response or the response cannot be parsed
    List<Station> list();

    /// Builder for constructing [StationsApi] instances.
    interface Builder {
        /// Sets the URL to fetch station data from.
        ///
        /// If not specified, the default URL for station data is used.
        ///
        /// @param url the URL for station data
        /// @return this `Builder` instance
        /// @throws NullPointerException if `url` is `null`
        Builder url(String url);

        /// Builds a configured `StationsApi` instance.
        ///
        /// @return a new `StationsApi`
        /// @throws Cta4jTrainException if the configured URL is invalid
        StationsApi build();
    }

    /// Creates a new `Builder` for constructing a `StationsApi`.
    ///
    /// @return a new `Builder`
    static Builder builder() {
        return new StationsApiImpl.BuilderImpl();
    }
}
