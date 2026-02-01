package com.cta4j.train.internal.impl;

import com.cta4j.internal.json.Cta4jObjectMapper;
import com.cta4j.train.TrainApi;
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
    private final TrainApiContext context;
    private final StationsApi stationsApi;

    public TrainApiImpl(
        String host,
        String apiKey,
        ObjectMapper objectMapper
    ) {
        Objects.requireNonNull(host);
        Objects.requireNonNull(apiKey);
        Objects.requireNonNull(objectMapper);

        this.context = new TrainApiContext(host, apiKey, objectMapper);
        this.stationsApi = new StationsApiImpl(this.context);
    }

    @Override
    public StationsApi stations() {
        return this.stationsApi;
    }

    public static final class BuilderImpl implements TrainApi.Builder {
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
        public TrainApi build() {
            String finalHost = Objects.requireNonNullElse(this.host, ApiUtils.DEFAULT_HOST);
            ObjectMapper objectMapper = Cta4jObjectMapper.instance();

            return new TrainApiImpl(finalHost, this.apiKey, objectMapper);
        }
    }
}
