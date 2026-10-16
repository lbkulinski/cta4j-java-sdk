package com.cta4j.train.station.internal.impl;

import com.cta4j.train.common.exception.Cta4jTrainException;
import com.cta4j.train.common.internal.util.TrainApiConstants;
import com.cta4j.train.station.StationsApi;
import com.cta4j.train.station.internal.mapper.StationMapper;
import com.cta4j.train.station.internal.wire.CtaStation;
import com.cta4j.train.station.model.Station;
import org.apache.hc.client5.http.fluent.Request;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class StationsApiImpl implements StationsApi {
    private static final TypeReference<List<CtaStation>> TYPE_REFERENCE = new TypeReference<>() {};

    private final URI uri;

    public StationsApiImpl(URI uri) {
        this.uri = Objects.requireNonNull(uri);
    }

    @Override
    public List<Station> list() {
        String response;

        try {
            response = Request.get(this.uri)
                              .execute()
                              .returnContent()
                              .asString();
        } catch (IOException e) {
            String message = Objects.requireNonNullElse(e.getMessage(), "Request failed");

            throw new Cta4jTrainException(message, this.uri.toString(), e);
        }

        List<CtaStation> stations;

        try {
            stations = JsonMapper.shared().readValue(response, TYPE_REFERENCE);
        } catch (JacksonException e) {
            throw new Cta4jTrainException("Failed to parse response", this.uri.toString(), e);
        }

        return stations.stream()
                       .map(StationMapper.INSTANCE::toDomain)
                       .toList();
    }

    public static final class BuilderImpl implements StationsApi.Builder {
        @Nullable
        private String url;

        public BuilderImpl() {
            this.url = null;
        }

        @Override
        public Builder url(String url) {
            this.url = Objects.requireNonNull(url);

            return this;
        }

        @Override
        public StationsApi build() {
            String finalUrl = Objects.requireNonNullElse(this.url, TrainApiConstants.DEFAULT_STATIONS_URL);

            URI uri;

            try {
                uri = URI.create(finalUrl);
            } catch (IllegalArgumentException e) {
                throw new Cta4jTrainException("Invalid stations URL", finalUrl, e);
            }

            return new StationsApiImpl(uri);
        }
    }
}
