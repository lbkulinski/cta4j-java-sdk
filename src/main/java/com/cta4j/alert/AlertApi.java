package com.cta4j.alert;

import com.cta4j.alert.common.internal.impl.AlertApiImpl;
import com.cta4j.alert.routestatus.RouteStatusApi;
import org.jspecify.annotations.NullMarked;

/**
 * Primary entry point for interacting with the CTA Alerts API.
 * <p>
 * This interface provides grouped sub-APIs for different aspects of the CTA Alerts API, such as route status and
 * detailed alerts.
 * <p>
 * Instances of {@code AlertApi} are immutable and thread-safe once built.
 * Use {@link #builder()} to construct a configured instance.
 */
@NullMarked
public interface AlertApi {
    /**
     * Provides access to route status-related endpoints.
     *
     * @return the {@link RouteStatusApi}
     */
    RouteStatusApi routeStatus();

    /**
     * Builder for constructing {@link AlertApi} instances.
     */
    interface Builder {
        /**
         * Sets the API host to use for requests.
         * <p>
         * If not specified, the default CTA Alerts API host is used.
         *
         * @param host the API host
         * @return this builder instance
         * @throws NullPointerException if {@code host} is {@code null}
         */
        Builder host(String host);

        /**
         * Builds a configured {@link AlertApi} instance.
         *
         * @return a new {@link AlertApi}
         */
        AlertApi build();
    }

    /**
     * Creates a new {@link Builder} for constructing a {@link AlertApi}.
     *
     * @return a new {@link Builder}
     */
    static Builder builder() {
        return new AlertApiImpl.BuilderImpl();
    }
}
