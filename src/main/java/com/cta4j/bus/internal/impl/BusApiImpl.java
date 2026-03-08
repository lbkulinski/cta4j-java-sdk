package com.cta4j.bus.internal.impl;

import com.cta4j.bus.internal.context.BusApiContext;
import com.cta4j.bus.internal.util.ApiUtils;
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
import com.cta4j.bus.internal.wire.CtaBustimeResponse;
import com.cta4j.bus.internal.wire.CtaError;
import com.cta4j.bus.internal.wire.CtaResponse;
import com.cta4j.bus.internal.mapper.Qualifiers;
import com.cta4j.exception.Cta4jException;
import com.cta4j.common.internal.http.HttpClient;
import com.cta4j.common.internal.json.Cta4jObjectMapper;
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

    private final BusApiContext context;
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
        String apiKey,
        ObjectMapper objectMapper
    ) {
        Objects.requireNonNull(host);
        Objects.requireNonNull(apiKey);
        Objects.requireNonNull(objectMapper);

        this.context = new BusApiContext(host, apiKey, objectMapper);
        this.vehiclesApi = new VehiclesApiImpl(this.context);
        this.routesApi = new RoutesApiImpl(this.context);
        this.directionsApi = new DirectionsApiImpl(this.context);
        this.stopsApi = new StopsApiImpl(this.context);
        this.patternsApi = new PatternsApiImpl(this.context);
        this.predictionsApi = new PredictionsApiImpl(this.context);
        this.localesApi = new LocalesApiImpl(this.context);
        this.detoursApi = new DetoursApiImpl(this.context);
    }

    @Override
    public Instant systemTime() {
        String url = new URIBuilder()
            .setScheme(ApiUtils.SCHEME)
            .setHost(this.context.host())
            .setPath(SYSTEM_TIME_ENDPOINT)
            .addParameter("key", this.context.apiKey())
            .addParameter("format", "json")
            .toString();

        String response = HttpClient.get(url);

        TypeReference<CtaResponse<String>> typeReference = new TypeReference<>() {};
        CtaResponse<String> timeResponse;

        try {
            timeResponse = this.context.objectMapper()
                                       .readValue(response, typeReference);
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

        Instant systemInstant = Qualifiers.mapTimestamp(systemTime);

        if (systemInstant == null) {
            String message = String.format(
                "Failed to map system time '%s' to Instant from %s",
                systemTime,
                SYSTEM_TIME_ENDPOINT
            );

            throw new Cta4jException(message);
        }

        return systemInstant;
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
            String finalHost = Objects.requireNonNullElse(this.host, ApiUtils.DEFAULT_HOST);
            ObjectMapper objectMapper = Cta4jObjectMapper.instance();

            return new BusApiImpl(finalHost, this.apiKey, objectMapper);
        }
    }
}
