package com.cta4j.train.location.internal.impl;

import com.cta4j.common.internal.http.HttpClient;
import com.cta4j.exception.Cta4jException;
import com.cta4j.train.common.internal.config.TrainApiConfig;
import com.cta4j.train.common.internal.util.ApiUtils;
import com.cta4j.train.common.internal.wire.CtaError;
import com.cta4j.train.common.internal.wire.CtaResponse;
import com.cta4j.train.common.model.TrainLine;
import com.cta4j.train.location.LocationsApi;
import com.cta4j.train.location.internal.mapper.TrainLocationsMapper;
import com.cta4j.train.location.internal.wire.CtaLocationResponse;
import com.cta4j.train.location.model.TrainLocations;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class LocationsApiImpl implements LocationsApi {
    private static final String POSITIONS_ENDPOINT = "%s/ttpositions.aspx".formatted(ApiUtils.API_PREFIX);
    private static final TypeReference<CtaResponse<CtaLocationResponse>> TYPE_REFERENCE = new TypeReference<>() {};

    private final TrainApiConfig config;

    public LocationsApiImpl(TrainApiConfig config) {
        this.config = Objects.requireNonNull(config);
    }

    @Override
    public List<TrainLocations> findByLines(List<TrainLine> lines) {
        Objects.requireNonNull(lines);

        if (lines.isEmpty()) {
            return List.of();
        }

        lines.forEach(Objects::requireNonNull);

        List<String> lineCodes = lines.stream()
                                      .map(TrainLine::getCode)
                                      .toList();

        String linesString = String.join(",", lineCodes);

        String url = new URIBuilder()
            .setScheme(this.config.scheme())
            .setHost(this.config.host())
            .setPort(this.config.port())
            .setPath(POSITIONS_ENDPOINT)
            .addParameter("rt", linesString)
            .addParameter("key", this.config.apiKey())
            .addParameter("outputType", "JSON")
            .toString();

        return this.makeRequest(url);
    }

    private List<TrainLocations> makeRequest(String url) {
        String response = HttpClient.get(url);

        CtaResponse<CtaLocationResponse> ctaResponse;

        try {
            ctaResponse = JsonMapper.shared()
                                    .readValue(response, TYPE_REFERENCE);
        } catch (JacksonException e) {
            String message = "Failed to parse response from %s".formatted(POSITIONS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaLocationResponse locationResponse = ctaResponse.ctatt();

        int errCd = ApiUtils.parseErrCd(locationResponse.errCd(), POSITIONS_ENDPOINT);

        if (errCd != 0) {
            CtaError error = new CtaError(errCd, locationResponse.errNm());

            String message = ApiUtils.buildErrorMessage(POSITIONS_ENDPOINT, error);

            throw new Cta4jException(message);
        }

        if (locationResponse.route() == null) {
            return List.of();
        }

        return locationResponse.route()
                               .stream()
                               .map(TrainLocationsMapper.INSTANCE::toDomain)
                               .toList();
    }
}
