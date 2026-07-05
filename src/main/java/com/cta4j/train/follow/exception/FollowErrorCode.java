package com.cta4j.train.follow.exception;

import org.jspecify.annotations.NullMarked;

/**
 * Represents the error codes returned by the CTA Follow API.
 */
@NullMarked
public enum FollowErrorCode {
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
     * Indicates that the query string contains a parameter that is not recognized by the API. The supported API
     * parameters are "runnumber" and "key".
     */
    INVALID_PARAMETER(500),

    /**
     * Indicates that the specified run number does not correspond to any known train run.
     */
    RUN_NOT_FOUND(501),

    /**
     * Indicates that the specified train run exists, but has an unexpected exit station ID that prevents the API from
     * reliably determining which predictions to report.
     */
    UNABLE_TO_DETERMINE_STOPS(502),

    /**
     * Indicates that the specified train run exists, but none of its available predictions are for active stations.
     */
    UNABLE_TO_FIND_PREDICTIONS(503),

    /**
     * Indicates that an unknown error occurred that does not match any of the defined error codes.
     */
    UNKNOWN(-1);

    /**
     * The integer code associated with this error code.
     */
    private final int code;

    /**
     * Constructs a {@code FollowErrorCode}.
     *
     * @param code the integer code associated with the error code
     */
    FollowErrorCode(int code) {
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
     * Returns the {@code FollowErrorCode} corresponding to the given integer code.
     *
     * @param code the integer code to look up
     * @return the corresponding {@code FollowErrorCode}, or {@code UNKNOWN} if the code does not match any defined
     * error code
     */
    public static FollowErrorCode fromCode(int code) {
        return switch (code) {
            case 0 -> OK;
            case 100 -> MISSING_PARAMETER;
            case 101 -> INVALID_API_KEY;
            case 102 -> DAILY_LIMIT_EXCEEDED;
            case 500 -> INVALID_PARAMETER;
            case 501 -> RUN_NOT_FOUND;
            case 502 -> UNABLE_TO_DETERMINE_STOPS;
            case 503 -> UNABLE_TO_FIND_PREDICTIONS;
            default -> UNKNOWN;
        };
    }
}
