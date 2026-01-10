package com.cta4j.bus.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;

@NullMarked
@SuppressWarnings("ConstantConditions")
public record Stop(
    String id,

    String name,

    BigDecimal latitude,

    BigDecimal longitude,

    @Nullable
    List<Integer> detoursAdded,

    @Nullable
    List<Integer> detoursRemoved,

    @Nullable
    Integer gtfsSequence,

    @Nullable
    Boolean adaAccessible
) {
}
