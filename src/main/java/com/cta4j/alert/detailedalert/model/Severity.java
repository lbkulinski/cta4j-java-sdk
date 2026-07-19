package com.cta4j.alert.detailedalert.model;

import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public record Severity(
    int score,
    String color,
    String css
) {
    public Severity {
        Objects.requireNonNull(color);
        Objects.requireNonNull(css);

        if (score < 0 || score > 99) {
            throw new IllegalArgumentException("score must be between 0 and 99 (inclusive)");
        }
    }
}
