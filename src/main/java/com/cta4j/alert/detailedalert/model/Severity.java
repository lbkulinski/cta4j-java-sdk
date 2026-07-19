package com.cta4j.alert.detailedalert.model;

import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * Represents the severity of an alert.
 *
 * @param score the numerical score used to rank this severity, based on the alert's impact on overall service, between
 *              0 and 99 (inclusive)
 * @param color the hexadecimal RGB color code used to color this severity's text on transitchicago.com, as
 * {@code rrggbb}
 * @param css the category used to pick the icon and display style of the alert (e.g., "normal", "planned", "minor",
 *            "major"); note that this set is not exhaustive, as other values (e.g., "special-note") have been observed
 *            in practice that are not documented in the CTA Alerts API documentation
 */
@NullMarked
public record Severity(
    int score,
    String color,
    String css
) {
    /**
     * Constructs a {@code Severity}.
     *
     * @param score the numerical score used to rank the severity, based on the alert's impact on overall
     *              service, between 0 and 99 (inclusive)
     * @param color the hexadecimal RGB color code used to color the severity's text on transitchicago.com, as
     *              {@code rrggbb}
     * @param css the category used to pick the icon and display style of the alert (e.g., "normal", "planned",
     *            "minor", "major")
     * @throws NullPointerException if {@code color} or {@code css} is {@code null}
     * @throws IllegalArgumentException if {@code score} is not between 0 and 99 (inclusive)
     */
    public Severity {
        Objects.requireNonNull(color);
        Objects.requireNonNull(css);

        if (score < 0 || score > 99) {
            throw new IllegalArgumentException("score must be between 0 and 99 (inclusive)");
        }
    }
}
