package com.cta4j.train.common.internal.impl;

import com.cta4j.train.TrainApi;
import com.cta4j.train.arrival.ArrivalsApi;
import com.cta4j.train.arrival.internal.impl.ArrivalsApiImpl;
import com.cta4j.train.common.internal.config.TrainApiConfig;
import com.cta4j.train.common.internal.util.TrainApiConstants;
import com.cta4j.train.follow.FollowApi;
import com.cta4j.train.follow.internal.impl.FollowApiImpl;
import com.cta4j.train.location.LocationsApi;
import com.cta4j.train.location.internal.impl.LocationsApiImpl;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class TrainApiImpl implements TrainApi {
    private final ArrivalsApi arrivalsApi;
    private final FollowApi followApi;
    private final LocationsApi locationsApi;

    public TrainApiImpl(TrainApiConfig config) {
        Objects.requireNonNull(config);

        this.arrivalsApi = new ArrivalsApiImpl(config);
        this.followApi = new FollowApiImpl(config);
        this.locationsApi = new LocationsApiImpl(config);
    }

    @Override
    public ArrivalsApi arrivals() {
        return this.arrivalsApi;
    }

    @Override
    public FollowApi follow() {
        return this.followApi;
    }

    @Override
    public LocationsApi locations() {
        return this.locationsApi;
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
            String finalHost = Objects.requireNonNullElse(this.host, TrainApiConstants.DEFAULT_HOST);

            TrainApiConfig config = new TrainApiConfig(TrainApiConstants.SCHEME, finalHost, this.apiKey);

            return new TrainApiImpl(config);
        }
    }
}
