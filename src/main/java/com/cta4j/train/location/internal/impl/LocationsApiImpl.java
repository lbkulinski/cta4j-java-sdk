package com.cta4j.train.location.internal.impl;

import com.cta4j.common.internal.http.HttpClient;
import com.cta4j.exception.Cta4jException;
import com.cta4j.train.common.internal.context.TrainApiContext;
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

import java.util.List;
import java.util.Objects;

@NullMarked
@ApiStatus.Internal
public final class LocationsApiImpl implements LocationsApi {
    private static final String POSITIONS_ENDPOINT = String.format("%s/ttpositions.aspx", ApiUtils.API_PREFIX);

    private final TrainApiContext context;

    public LocationsApiImpl(TrainApiContext context) {
        this.context = context;
    }

    @Override
    public List<TrainLocations> findByLines(List<TrainLine> lines) {
        Objects.requireNonNull(lines);

        List<String> lineCodes = lines.stream()
                                      .map(TrainLine::getCode)
                                      .toList();

        String linesString = String.join(",", lineCodes);

        String url = new URIBuilder()
            .setScheme(ApiUtils.SCHEME)
            .setHost(this.context.host())
            .setPath(POSITIONS_ENDPOINT)
            .addParameter("rt", linesString)
            .addParameter("key", this.context.apiKey())
            .addParameter("outputType", "JSON")
            .toString();

        return this.makeRequest(url);
    }

    private List<TrainLocations> makeRequest(String url) {
        String response = HttpClient.get(url);

        TypeReference<CtaResponse<CtaLocationResponse>> typeReference = new TypeReference<>() {};
        CtaResponse<CtaLocationResponse> ctaResponse;

        try {
            ctaResponse = this.context.objectMapper()
                                      .readValue(response, typeReference);
        } catch (JacksonException e) {
            String message = String.format("Failed to parse response from %s", POSITIONS_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaLocationResponse locationResponse = ctaResponse.ctatt();

        if (locationResponse.errCd() != 0) {
            CtaError error = new CtaError(locationResponse.errCd(), locationResponse.errNm());

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
