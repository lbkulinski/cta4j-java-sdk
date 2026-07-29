package com.cta4j.alert;

import com.cta4j.alert.common.internal.impl.AlertApiImpl;
import com.cta4j.alert.detailedalert.DetailedAlertsApi;
import com.cta4j.alert.routestatus.RouteStatusApi;
import org.jspecify.annotations.NullMarked;

/// Primary entry point for interacting with the CTA Alerts API.
///
/// This interface provides grouped sub-APIs for different aspects of the CTA Alerts API, such as route status and
/// detailed alerts.
///
/// Instances of `AlertApi` are immutable and thread-safe once built.
/// Use [#builder()] to construct a configured instance.
@NullMarked
public interface AlertApi {
    /// Provides access to route status-related endpoints.
    ///
    /// @return the [RouteStatusApi]
    RouteStatusApi routeStatus();

    /// Provides access to detailed alert-related endpoints.
    ///
    /// @return the [DetailedAlertsApi]
    DetailedAlertsApi detailedAlerts();

    /// Builder for constructing [AlertApi] instances.
    interface Builder {
        /// Sets the API host to use for requests.
        ///
        /// If not specified, the default CTA Alerts API host is used.
        ///
        /// @param host the API host
        /// @return this builder instance
        /// @throws NullPointerException if `host` is `null`
        Builder host(String host);

        /// Builds a configured [AlertApi] instance.
        ///
        /// @return a new [AlertApi]
        AlertApi build();
    }

    /// Creates a new [Builder] for constructing a [AlertApi].
    ///
    /// @return a new [Builder]
    static Builder builder() {
        return new AlertApiImpl.BuilderImpl();
    }
}
