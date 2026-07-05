package com.cta4j.train.location.internal.impl;

import com.cta4j.train.common.internal.config.TrainApiConfig;
import com.cta4j.train.common.internal.util.TrainApiConstants;
import com.cta4j.train.common.internal.wire.CtaResponse;
import com.cta4j.train.common.model.TrainLine;
import com.cta4j.train.location.LocationsApi;
import com.cta4j.train.location.exception.Cta4jLocationsException;
import com.cta4j.train.location.exception.LocationsErrorCode;
import com.cta4j.train.location.internal.mapper.TrainLocationsMapper;
import com.cta4j.train.location.internal.wire.CtaLocationResponse;
import com.cta4j.train.location.model.TrainLocations;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class LocationsApiImpl implements LocationsApi {
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

        List<String> lineCodes = List.copyOf(lines)
                                     .stream()
                                     .map(TrainLine::getCode)
                                     .toList();

        String linesString = String.join(",", lineCodes);

        String url = new URIBuilder()
            .setScheme(this.config.scheme())
            .setHost(this.config.host())
            .setPort(this.config.port())
            .setPath(TrainApiConstants.POSITIONS_ENDPOINT)
            .addParameter("rt", linesString)
            .addParameter("key", this.config.apiKey())
            .addParameter("outputType", "JSON")
            .toString();

        return this.makeRequest(url);
    }

    private List<TrainLocations> makeRequest(String url) {
        String response;

        try {
            response = Request.get(url)
                              .execute()
                              .returnContent()
                              .asString();
        } catch (IOException e) {
            String message = e.getMessage();

            throw new Cta4jLocationsException(message, e);
        }

        CtaResponse<CtaLocationResponse> ctaResponse;

        try {
            ctaResponse = JsonMapper.shared()
                                    .readValue(response, TYPE_REFERENCE);
        } catch (JacksonException e) {
            throw new Cta4jLocationsException("Failed to parse response", e);
        }

        CtaLocationResponse locationResponse = ctaResponse.ctatt();

        int errCd;

        try {
            errCd = Integer.parseInt(locationResponse.errCd());
        } catch (NumberFormatException e) {
            throw new Cta4jLocationsException("Failed to parse error code", e);
        }

        if (errCd < 0) {
            throw new Cta4jLocationsException("Unknown error code", errCd);
        }

        LocationsErrorCode errorCode = LocationsErrorCode.fromCode(errCd);

        if (errorCode != LocationsErrorCode.OK) {
            String errNm = locationResponse.errNm();

            String message = (errNm == null || errNm.isBlank())
                ? "An unknown error occurred."
                : errNm;

            throw new Cta4jLocationsException(message, errCd);
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
