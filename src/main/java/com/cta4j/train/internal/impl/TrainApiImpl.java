package com.cta4j.train.internal.impl;

import com.cta4j.internal.json.Cta4jObjectMapper;
import com.cta4j.train.TrainApi;
import com.cta4j.train.arrival.ArrivalsApi;
import com.cta4j.train.arrival.internal.impl.ArrivalsApiImpl;
import com.cta4j.train.internal.context.TrainApiContext;
import com.cta4j.train.internal.util.ApiUtils;
import com.cta4j.train.station.StationsApi;
import com.cta4j.train.station.internal.impl.StationsApiImpl;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;

import java.util.Objects;

@NullMarked
@ApiStatus.Internal
public final class TrainApiImpl implements TrainApi {
    private final StationsApi stationsApi;
    private final ArrivalsApi arrivalsApi;

    public TrainApiImpl(
        String host,
        String stationsUrl,
        String apiKey,
        ObjectMapper objectMapper
    ) {
        Objects.requireNonNull(host);
        Objects.requireNonNull(apiKey);
        Objects.requireNonNull(objectMapper);

        TrainApiContext context = new TrainApiContext(host, stationsUrl, apiKey, objectMapper);
        this.stationsApi = new StationsApiImpl(context);
        this.arrivalsApi = new ArrivalsApiImpl(context);
    }

    @Override
    public StationsApi stations() {
        return this.stationsApi;
    }

    @Override
    public ArrivalsApi arrivals() {
        return this.arrivalsApi;
    }

    public static final class BuilderImpl implements TrainApi.Builder {
        private final String apiKey;

        @Nullable
        private String host;

        @Nullable
        private String stationsUrl;

        public BuilderImpl(String apiKey) {
            this.apiKey = Objects.requireNonNull(apiKey);
            this.host = null;
            this.stationsUrl = null;
        }

        @Override
        public Builder host(String host) {
            this.host = Objects.requireNonNull(host);

            return this;
        }

        @Override
        public Builder stationsUrl(String stationsUrl) {
            this.stationsUrl = Objects.requireNonNull(stationsUrl);

            return this;
        }

        @Override
        public TrainApi build() {
            String finalHost = Objects.requireNonNullElse(this.host, ApiUtils.DEFAULT_HOST);
            String finalStationsUrl = Objects.requireNonNullElse(this.stationsUrl, ApiUtils.DEFAULT_STATIONS_URL);
            ObjectMapper objectMapper = Cta4jObjectMapper.instance();

            return new TrainApiImpl(finalHost, finalStationsUrl, this.apiKey, objectMapper);
        }
    }
}
