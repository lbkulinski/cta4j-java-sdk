package com.cta4j.alert.common.internal.mapper;

import com.cta4j.common.train.TrainLine;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.mapstruct.Named;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class Qualifiers {
    private Qualifiers() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Named("mapUri")
    public static URI mapUri(String value) {
        Objects.requireNonNull(value);

        try {
            return new URI(value);
        } catch (URISyntaxException e) {
            String message = "Failed to parse URI: %s".formatted(value);

            throw new IllegalArgumentException(message, e);
        }
    }

    @Named("mapTrainLine")
    public static TrainLine mapTrainLine(String code) {
        Objects.requireNonNull(code);

        try {
            return TrainLine.fromCode(code);
        } catch (IllegalArgumentException e) {
            String message = "Failed to parse train line code: %s".formatted(code);

            throw new IllegalArgumentException(message, e);
        }
    }
}
