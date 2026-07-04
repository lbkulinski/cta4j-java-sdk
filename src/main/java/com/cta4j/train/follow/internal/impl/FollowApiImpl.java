package com.cta4j.train.follow.internal.impl;

import com.cta4j.train.common.internal.config.TrainApiConfig;
import com.cta4j.train.common.internal.util.ApiUtils;
import com.cta4j.train.common.internal.util.TrainApiConstants;
import com.cta4j.train.common.internal.wire.CtaResponse;
import com.cta4j.train.follow.FollowApi;
import com.cta4j.train.follow.exception.Cta4jFollowException;
import com.cta4j.train.follow.exception.FollowErrorCode;
import com.cta4j.train.follow.internal.mapper.FollowTrainMapper;
import com.cta4j.train.follow.internal.wire.CtaFollowResponse;
import com.cta4j.train.follow.model.FollowTrain;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@ApiStatus.Internal
@NullMarked
public final class FollowApiImpl implements FollowApi {
    private static final TypeReference<CtaResponse<CtaFollowResponse>> TYPE_REFERENCE = new TypeReference<>() {};

    private final TrainApiConfig config;

    public FollowApiImpl(TrainApiConfig config) {
        this.config = Objects.requireNonNull(config);
    }

    @Override
    public Optional<FollowTrain> findByRun(String run) {
        Objects.requireNonNull(run);

        String url = new URIBuilder()
            .setScheme(this.config.scheme())
            .setHost(this.config.host())
            .setPort(this.config.port())
            .setPath(TrainApiConstants.FOLLOW_ENDPOINT)
            .addParameter("runnumber", run)
            .addParameter("key", this.config.apiKey())
            .addParameter("outputType", "JSON")
            .toString();

        return this.makeRequest(url);
    }

    private Optional<FollowTrain> makeRequest(String url) {
        String response;

        try {
            response = Request.get(url)
                              .execute()
                              .returnContent()
                              .asString();
        } catch (IOException e) {
            String message = e.getMessage();

            throw new Cta4jFollowException(message, e);
        }

        CtaResponse<CtaFollowResponse> ctaResponse;

        try {
            ctaResponse = JsonMapper.shared()
                                    .readValue(response, TYPE_REFERENCE);
        } catch (JacksonException e) {
            throw new Cta4jFollowException("Failed to parse response", e);
        }

        CtaFollowResponse followResponse = ctaResponse.ctatt();

        int errCd = ApiUtils.parseErrCd(followResponse.errCd(), TrainApiConstants.FOLLOW_ENDPOINT);

        FollowErrorCode errorCode = FollowErrorCode.fromCode(errCd);

        if (errorCode == FollowErrorCode.RUN_NOT_FOUND) {
            return Optional.empty();
        }

        if (errorCode != FollowErrorCode.OK) {
            String message = Objects.requireNonNullElse(followResponse.errNm(), "An unknown error occurred.");

            throw new Cta4jFollowException(message, errCd);
        }

        FollowTrain followTrain = FollowTrainMapper.INSTANCE.toDomain(followResponse);

        return Optional.of(followTrain);
    }
}
