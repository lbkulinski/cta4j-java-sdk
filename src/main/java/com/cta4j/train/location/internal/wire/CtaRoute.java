package com.cta4j.train.location.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

@NullMarked
@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaRoute(
    @JsonProperty("@name")
    String name,

    @Nullable
    List<CtaLocationTrain> train
) {
    public CtaRoute {
        Objects.requireNonNull(name);

        if (train != null) {
            train = List.copyOf(train);
        }
    }
}
