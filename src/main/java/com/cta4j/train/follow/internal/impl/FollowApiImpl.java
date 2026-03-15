package com.cta4j.train.follow.internal.impl;

import com.cta4j.common.internal.http.HttpClient;
import com.cta4j.exception.Cta4jException;
import com.cta4j.train.follow.FollowApi;
import com.cta4j.train.follow.internal.mapper.FollowTrainMapper;
import com.cta4j.train.follow.internal.wire.CtaFollowResponse;
import com.cta4j.train.follow.model.FollowTrain;
import com.cta4j.train.common.internal.context.TrainApiContext;
import com.cta4j.train.common.internal.util.ApiUtils;
import com.cta4j.train.common.internal.wire.CtaError;
import com.cta4j.train.common.internal.wire.CtaResponse;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;

import java.util.Objects;
import java.util.Optional;

@NullMarked
@ApiStatus.Internal
public final class FollowApiImpl implements FollowApi {
    private static final String FOLLOW_ENDPOINT = String.format("%s/ttfollow.aspx", ApiUtils.API_PREFIX);
    private static final int NOT_FOUND_ERROR_CODE = 501;

    private final TrainApiContext context;

    public FollowApiImpl(TrainApiContext context) {
        this.context = Objects.requireNonNull(context);
    }

    @Override
    public Optional<FollowTrain> findByRun(String run) {
        Objects.requireNonNull(run);

        String url = new URIBuilder()
            .setScheme(ApiUtils.SCHEME)
            .setHost(this.context.host())
            .setPath(FOLLOW_ENDPOINT)
            .addParameter("runnumber", run)
            .addParameter("key", this.context.apiKey())
            .addParameter("outputType", "JSON")
            .toString();

        return this.makeRequest(url);
    }

    private Optional<FollowTrain> makeRequest(String url) {
        String response = HttpClient.get(url);

        TypeReference<CtaResponse<CtaFollowResponse>> typeReference = new TypeReference<>() {};
        CtaResponse<CtaFollowResponse> ctaResponse;

        try {
            ctaResponse = this.context.objectMapper()
                                      .readValue(response, typeReference);
        } catch (JacksonException e) {
            String message = String.format("Failed to parse response from %s", FOLLOW_ENDPOINT);

            throw new Cta4jException(message, e);
        }

        CtaFollowResponse followResponse = ctaResponse.ctatt();

        if (followResponse.errCd() == NOT_FOUND_ERROR_CODE) {
            return Optional.empty();
        }

        if (followResponse.errCd() != 0) {
            String errorMessage = Objects.requireNonNullElse(followResponse.errNm(), "Unknown error");

            CtaError error = new CtaError(followResponse.errCd(), errorMessage);

            String message = ApiUtils.buildErrorMessage(FOLLOW_ENDPOINT, error);

            throw new Cta4jException(message);
        }

        FollowTrain followTrain = FollowTrainMapper.INSTANCE.toDomain(followResponse);

        return Optional.of(followTrain);
    }
}
