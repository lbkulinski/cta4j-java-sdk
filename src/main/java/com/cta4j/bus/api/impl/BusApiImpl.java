package com.cta4j.bus.api.impl;

import com.cta4j.bus.api.ApiUtils;
import com.cta4j.bus.api.BusApi;
import com.cta4j.bus.api.DetoursApi;
import com.cta4j.bus.api.DirectionsApi;
import com.cta4j.bus.api.LocalesApi;
import com.cta4j.bus.api.PatternsApi;
import com.cta4j.bus.api.PredictionsApi;
import com.cta4j.bus.api.RoutesApi;
import com.cta4j.bus.api.StopsApi;
import com.cta4j.bus.api.vehicle.VehiclesApi;
import com.cta4j.bus.api.vehicle.impl.VehiclesApiImpl;
import org.jspecify.annotations.NonNull;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;

public final class BusApiImpl implements BusApi {
    private final String host;
    private final String apiKey;
    private final ObjectMapper objectMapper;

    public BusApiImpl(String host, String apiKey) {
        if (host == null) {
            throw new IllegalArgumentException("host must not be null");
        }

        if (apiKey == null) {
            throw new IllegalArgumentException("apiKey must not be null");
        }

        this.host = host;
        this.apiKey = apiKey;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Instant systemTime() {
        return null;
    }

    @Override
    public VehiclesApi vehicles() {
        return new VehiclesApiImpl(this.host, this.apiKey, this.objectMapper);
    }

    @Override
    public RoutesApi routes() {
        return null;
    }

    @Override
    public DirectionsApi directions() {
        return null;
    }

    @Override
    public StopsApi stops() {
        return null;
    }

    @Override
    public PatternsApi patterns() {
        return null;
    }

    @Override
    public PredictionsApi predictions() {
        return null;
    }

    @Override
    public LocalesApi locales() {
        return null;
    }

    @Override
    public DetoursApi detours() {
        return null;
    }

    public static final class BuilderImpl implements BusApi.Builder {
        private String host;
        private String apiKey;

        public BuilderImpl() {
            this.host = null;
            this.apiKey = null;
        }

        @Override
        public @NonNull Builder host(String host) {
            if (host == null) {
                throw new IllegalArgumentException("host must not be null");
            }

            this.host = host;

            return this;
        }

        @Override
        public @NonNull Builder apiKey(String apiKey) {
            if (apiKey == null) {
                throw new IllegalArgumentException("apiKey must not be null");
            }

            this.apiKey = apiKey;

            return this;
        }

        @Override
        public BusApi build() {
            String finalHost = (this.host == null) ? ApiUtils.DEFAULT_HOST : this.host;

            if (this.apiKey == null) {
                throw new IllegalStateException("API key must not be null");
            }

            return new BusApiImpl(finalHost, this.apiKey);
        }
    }
}
