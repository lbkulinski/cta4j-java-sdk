package com.cta4j.train.location.exception;

import org.jspecify.annotations.NullMarked;

/**
 * Represents the error codes returned by the CTA Location API.
 */
@NullMarked
public enum LocationsErrorCode {
    /**
     * Indicates that the request was successful and there were no errors.
     */
    OK(0),

    /**
     * Indicates that a required parameter is missing from the request.
     */
    MISSING_PARAMETER(100),

    /**
     * Indicates that the provided API key is invalid.
     */
    INVALID_API_KEY(101),

    /**
     * Indicates that the daily limit for API requests has been exceeded.
     */
    DAILY_LIMIT_EXCEEDED(102),

    /**
     * Indicates that the specified route is not valid or does not exist in the CTA system. Valid route identifiers are
     * red, blue, brn, g, org, p, pink, and y. The route identifiers are case-insensitive.
     */
    INVALID_ROUTE(106),

    /**
     * Indicates that the number of routes provided exceeds the allowed limit (more than 8).
     */
    TOO_MANY_ROUTES(107),

    /**
     * Indicates that the query string contains a parameter that is not recognized by the API. The supported API
     * parameters are "rt" and "key".
     */
    INVALID_PARAMETER(500),

    /**
     * Indicates that an unknown error occurred that does not match any of the defined error codes.
     */
    UNKNOWN(-1);

    /**
     * The integer code associated with this error code.
     */
    private final int code;

    /**
     * Constructs an {@code LocationsErrorCode}.
     *
     * @param code the integer code associated with the error code
     */
    LocationsErrorCode(int code) {
        this.code = code;
    }

    /**
     * Returns the integer code associated with this error code.
     *
     * @return the integer code
     */
    public int getCode() {
        return this.code;
    }

    /**
     * Returns the {@code LocationsErrorCode} corresponding to the given integer code.
     *
     * @param code the integer code to look up
     * @return the corresponding {@code LocationsErrorCode}, or {@code UNKNOWN} if the code does not match any defined
     * error code
     */
    public static LocationsErrorCode fromCode(int code) {
        return switch (code) {
            case 0 -> OK;
            case 100 -> MISSING_PARAMETER;
            case 101 -> INVALID_API_KEY;
            case 102 -> DAILY_LIMIT_EXCEEDED;
            case 106 -> INVALID_ROUTE;
            case 107 -> TOO_MANY_ROUTES;
            case 500 -> INVALID_PARAMETER;
            default -> UNKNOWN;
        };
    }
}
