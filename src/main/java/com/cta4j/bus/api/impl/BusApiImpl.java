package com.cta4j.bus.api.impl;

import com.cta4j.bus.api.common.util.ApiUtils;
import com.cta4j.bus.api.BusApi;
import com.cta4j.bus.api.detour.DetoursApi;
import com.cta4j.bus.api.detour.impl.DetoursApiImpl;
import com.cta4j.bus.api.direction.DirectionsApi;
import com.cta4j.bus.api.locale.LocalesApi;
import com.cta4j.bus.api.locale.impl.LocalesApiImpl;
import com.cta4j.bus.api.pattern.PatternsApi;
import com.cta4j.bus.api.prediction.PredictionsApi;
import com.cta4j.bus.api.direction.impl.DirectionsApiImpl;
import com.cta4j.bus.api.pattern.impl.PatternsApiImpl;
import com.cta4j.bus.api.prediction.impl.PredictionsApiImpl;
import com.cta4j.bus.api.route.RoutesApi;
import com.cta4j.bus.api.stop.StopsApi;
import com.cta4j.bus.api.route.impl.RoutesApiImpl;
import com.cta4j.bus.api.stop.impl.StopsApiImpl;
import com.cta4j.bus.api.vehicle.VehiclesApi;
import com.cta4j.bus.api.vehicle.impl.VehiclesApiImpl;
import com.cta4j.bus.api.common.external.CtaBustimeResponse;
import com.cta4j.bus.api.common.external.CtaError;
import com.cta4j.bus.api.common.external.CtaResponse;
import com.cta4j.bus.api.common.util.CtaBusMappingQualifiers;
import com.cta4j.exception.Cta4jException;
import com.cta4j.util.HttpUtils;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@NullMarked
@ApiStatus.Internal
public final class BusApiImpl implements BusApi {
    private static final String SYSTEM_TIME_ENDPOINT = String.format("%s/gettime", ApiUtils.API_PREFIX);

    private final String host;
    private final String apiKey;
    private final ObjectMapper objectMapper;
    private final VehiclesApi vehiclesApi;
    private final RoutesApi routesApi;
    private final DirectionsApi directionsApi;
    private final StopsApi stopsApi;
    private final PatternsApi patternsApi;
    private final PredictionsApi predictionsApi;
    private final LocalesApi localesApi;
    private final DetoursApi detoursApi;

    public BusApiImpl(
        String host,
        String apiKey
    ) {
        this.host = Objects.requireNonNull(host);
        this.apiKey = Objects.requireNonNull(apiKey);
        this.objectMapper = new ObjectMapper();
        this.vehiclesApi = new VehiclesApiImpl(this.host, this.apiKey, this.objectMapper);
        this.routesApi = new RoutesApiImpl(this.host, this.apiKey, this.objectMapper);
        this.directionsApi = new DirectionsApiImpl(this.host, this.apiKey, this.objectMapper);
        this.stopsApi = new StopsApiImpl(this.host, this.apiKey, this.objectMapper);
        this.patternsApi = new PatternsApiImpl(this.host, this.apiKey, this.objectMapper);
        this.predictionsApi = new PredictionsApiImpl(this.host, this.apiKey, this.objectMapper);
        this.localesApi = new LocalesApiImpl(this.host, this.apiKey, this.objectMapper);
        this.detoursApi = new DetoursApiImpl(this.host, this.apiKey, this.objectMapper);
    }

    @Override
    public Instant systemTime() {
        String url = new URIBuilder()
            .setScheme(ApiUtils.SCHEME)
            .setHost(this.host)
            .setPath(SYSTEM_TIME_ENDPOINT)
            .addParameter("key", this.apiKey)
            .addParameter("format", "json")
            .toString();

        String response = HttpUtils.get(url);

        TypeReference<CtaResponse<String>> typeReference = new TypeReference<>() {};
        CtaResponse<String> timeResponse;

        try {
            timeResponse = this.objectMapper.readValue(response, typeReference);
        } catch (JacksonException e) {
            String message = String.format("Failed to parse response from %s", SYSTEM_TIME_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaBustimeResponse<String> bustimeResponse = timeResponse.bustimeResponse();

        List<CtaError> errors = bustimeResponse.error();
        String systemTime = bustimeResponse.data();

        if ((errors == null) && (systemTime == null)) {
            String message = String.format(
                "System time bustime response missing both error and data from %s",
                SYSTEM_TIME_ENDPOINT
            );

            throw new Cta4jException(message);
        }

        if ((errors != null) && !errors.isEmpty()) {
            String message = ApiUtils.buildErrorMessage(SYSTEM_TIME_ENDPOINT, errors);

            throw new Cta4jException(message);
        }

        if (systemTime == null) {
            String message = String.format("No system time data returned from %s", SYSTEM_TIME_ENDPOINT);

            throw new Cta4jException(message);
        }

        return CtaBusMappingQualifiers.mapTimestamp(systemTime);
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
            String finalHost = (this.host == null) ? ApiUtils.DEFAULT_HOST : this.host;

            return new BusApiImpl(finalHost, this.apiKey);
        }
    }
}
