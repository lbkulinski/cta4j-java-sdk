package com.cta4j.bus.api.stop.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;

@NullMarked
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
    public Stop(
        @Nullable String id,
        @Nullable String name,
        @Nullable BigDecimal latitude,
        @Nullable BigDecimal longitude,
        @Nullable List<@Nullable Integer> detoursAdded,
        @Nullable List<@Nullable Integer> detoursRemoved,
        @Nullable Integer gtfsSequence,
        @Nullable Boolean adaAccessible
    ) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }

        if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        }

        if (latitude == null) {
            throw new IllegalArgumentException("latitude must not be null");
        }

        if (longitude == null) {
            throw new IllegalArgumentException("longitude must not be null");
        }

        List<Integer> detoursAddedCopy = null;

        if (detoursAdded != null) {
            for (Integer detourId : detoursAdded) {
                if (detourId == null) {
                    throw new IllegalArgumentException("detoursAdded must not contain null values");
                }
            }

            detoursAddedCopy = List.copyOf(detoursAdded);
        }

        List<Integer> detoursRemovedCopy = null;

        if (detoursRemoved != null) {
            for (Integer detourId : detoursRemoved) {
                if (detourId == null) {
                    throw new IllegalArgumentException("detoursRemoved must not contain null values");
                }
            }

            detoursRemovedCopy = List.copyOf(detoursRemoved);
        }

        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.detoursAdded = detoursAddedCopy;
        this.detoursRemoved = detoursRemovedCopy;
        this.gtfsSequence = gtfsSequence;
        this.adaAccessible = adaAccessible;
    }
}
