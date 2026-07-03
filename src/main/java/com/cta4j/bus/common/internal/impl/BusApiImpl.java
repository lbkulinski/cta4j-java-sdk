package com.cta4j.bus.common.internal.impl;

import com.cta4j.bus.common.BusApiConstants;
import com.cta4j.bus.common.internal.config.BusApiConfig;
import com.cta4j.bus.BusApi;
import com.cta4j.bus.detour.DetoursApi;
import com.cta4j.bus.detour.internal.impl.DetoursApiImpl;
import com.cta4j.bus.direction.DirectionsApi;
import com.cta4j.bus.locale.LocalesApi;
import com.cta4j.bus.locale.internal.impl.LocalesApiImpl;
import com.cta4j.bus.pattern.PatternsApi;
import com.cta4j.bus.prediction.PredictionsApi;
import com.cta4j.bus.direction.internal.impl.DirectionsApiImpl;
import com.cta4j.bus.pattern.internal.impl.PatternsApiImpl;
import com.cta4j.bus.prediction.internal.impl.PredictionsApiImpl;
import com.cta4j.bus.route.RoutesApi;
import com.cta4j.bus.stop.StopsApi;
import com.cta4j.bus.route.internal.impl.RoutesApiImpl;
import com.cta4j.bus.stop.internal.impl.StopsApiImpl;
import com.cta4j.bus.vehicle.VehiclesApi;
import com.cta4j.bus.vehicle.internal.impl.VehiclesApiImpl;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class BusApiImpl implements BusApi {
    private final SystemTimeApiImpl systemTimeApi;
    private final VehiclesApi vehiclesApi;
    private final RoutesApi routesApi;
    private final DirectionsApi directionsApi;
    private final StopsApi stopsApi;
    private final PatternsApi patternsApi;
    private final PredictionsApi predictionsApi;
    private final LocalesApi localesApi;
    private final DetoursApi detoursApi;

    public BusApiImpl(BusApiConfig config) {
        Objects.requireNonNull(config);

        this.systemTimeApi = new SystemTimeApiImpl(config);
        this.vehiclesApi = new VehiclesApiImpl(config);
        this.routesApi = new RoutesApiImpl(config);
        this.directionsApi = new DirectionsApiImpl(config);
        this.stopsApi = new StopsApiImpl(config);
        this.patternsApi = new PatternsApiImpl(config);
        this.predictionsApi = new PredictionsApiImpl(config);
        this.localesApi = new LocalesApiImpl(config);
        this.detoursApi = new DetoursApiImpl(config);
    }

    @Override
    public Instant systemTime() {
        return this.systemTimeApi.systemTime();
    }

    @Override
    public VehiclesApi vehicles() {
        return this.vehiclesApi;
    }

    @Override
    public RoutesApi routes() {
        return this.routesApi;
    }

    @Override
    public DirectionsApi directions() {
        return this.directionsApi;
    }

    @Override
    public StopsApi stops() {
        return this.stopsApi;
    }

    @Override
    public PatternsApi patterns() {
        return this.patternsApi;
    }

    @Override
    public PredictionsApi predictions() {
        return this.predictionsApi;
    }

    @Override
    public LocalesApi locales() {
        return this.localesApi;
    }

    @Override
    public DetoursApi detours() {
        return this.detoursApi;
    }

    public static final class BuilderImpl implements BusApi.Builder {
        private final String apiKey;

        @Nullable
        private String host;

        public BuilderImpl(String apiKey) {
            this.apiKey = Objects.requireNonNull(apiKey);
            this.host = null;
        }

        @Override
        public Builder host(String host) {
            this.host = Objects.requireNonNull(host);

            return this;
        }

        @Override
        public BusApi build() {
            String finalHost = Objects.requireNonNullElse(this.host, BusApiConstants.DEFAULT_HOST);

            BusApiConfig config = new BusApiConfig(BusApiConstants.SCHEME, finalHost, this.apiKey);

            return new BusApiImpl(config);
        }
    }
}
