package com.cta4j.bus.prediction.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
@NullMarked
public record CtaPredictionBustimeResponse(
    @Nullable List<CtaPredictionError> error,
    @Nullable List<CtaPrediction> prd
) {}
