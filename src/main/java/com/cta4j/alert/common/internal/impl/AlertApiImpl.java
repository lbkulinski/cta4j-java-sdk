package com.cta4j.alert.common.internal.impl;

import com.cta4j.alert.AlertApi;
import com.cta4j.alert.common.internal.config.AlertApiConfig;
import com.cta4j.alert.common.internal.util.AlertApiConstants;
import com.cta4j.alert.routestatus.RouteStatusApi;
import com.cta4j.alert.routestatus.internal.impl.RouteStatusApiImpl;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class AlertApiImpl implements AlertApi {
    private final RouteStatusApi routeStatusApi;

    public AlertApiImpl(AlertApiConfig config) {
        Objects.requireNonNull(config);

        this.routeStatusApi = new RouteStatusApiImpl(config);
    }

    @Override
    public RouteStatusApi routeStatus() {
        return this.routeStatusApi;
    }

    public static final class BuilderImpl implements AlertApi.Builder {
        @Nullable
        private String host;

        public BuilderImpl() {
            this.host = null;
        }

        @Override
        public Builder host(String host) {
            this.host = Objects.requireNonNull(host);

            return this;
        }

        @Override
        public AlertApi build() {
            String finalHost = Objects.requireNonNullElse(this.host, AlertApiConstants.DEFAULT_HOST);

            AlertApiConfig config = new AlertApiConfig(AlertApiConstants.SCHEME, finalHost);

            return new AlertApiImpl(config);
        }
    }
}
