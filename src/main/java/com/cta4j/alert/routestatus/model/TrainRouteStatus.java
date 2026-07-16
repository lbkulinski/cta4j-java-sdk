package com.cta4j.alert.routestatus.model;

import com.cta4j.common.train.TrainLine;
import org.jspecify.annotations.NullMarked;

import java.net.URI;
import java.util.Objects;

/**
 * Represents the service status of a single train route.
 *
 * @param route the name of this route (e.g., "Red Line")
 * @param color the color of this route used in maps, as {@code rrggbb} (e.g., "c60c30")
 * @param textColor the suggested color of text displayed against {@code color}, as {@code rrggbb} (e.g., "ffffff")
 * @param line the {@link TrainLine} this status corresponds to
 * @param url the URL of this route's page on transitchicago.com
 * @param status the ultimate, human-readable status of this route (e.g., "Normal Service", "Service Change")
 * @param statusColor the suggested color associated with {@code status}, as {@code rrggbb} (e.g., "404040")
 */
@NullMarked
public record TrainRouteStatus(
    String route,
    String color,
    String textColor,
    TrainLine line,
    URI url,
    String status,
    String statusColor
) {
    /**
     * Constructs a {@code TrainRouteStatus}.
     *
     * @param route the name of the route (e.g., "Red Line")
     * @param color the color of the route used in maps, as {@code rrggbb} (e.g., "c60c30")
     * @param textColor the suggested color of text displayed against {@code color}, as {@code rrggbb} (e.g., "ffffff")
     * @param line the {@link TrainLine} the route corresponds to
     * @param url the URL of the route's page on transitchicago.com
     * @param status the ultimate, human-readable status of the route (e.g., "Normal Service", "Service Change")
     * @param statusColor the suggested color associated with {@code status}, as {@code rrggbb} (e.g., "404040")
     * @throws NullPointerException if {@code route}, {@code color}, {@code textColor}, {@code line},
     * {@code url}, {@code status}, or {@code statusColor} is {@code null}
     */
    public TrainRouteStatus {
        Objects.requireNonNull(route);
        Objects.requireNonNull(color);
        Objects.requireNonNull(textColor);
        Objects.requireNonNull(line);
        Objects.requireNonNull(url);
        Objects.requireNonNull(status);
        Objects.requireNonNull(statusColor);
    }
}
