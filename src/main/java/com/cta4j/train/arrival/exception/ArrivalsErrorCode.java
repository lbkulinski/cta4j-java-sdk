package com.cta4j.train.arrival.exception;

import org.jspecify.annotations.NullMarked;

/**
 * Represents the error codes returned by the CTA Arrivals API.
 */
@NullMarked
public enum ArrivalsErrorCode {
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
     * Indicates that the provided map ID is invalid.
     */
    INVALID_MAPID(103),

    /**
     * Indicates that the provided map ID is not an integer.
     */
    MAPID_NOT_INTEGER(104),

    /**
     * Indicates that the number of map IDs provided exceeds the allowed limit (more than 4).
     */
    TOO_MANY_MAPIDS(105),

    /**
     * Indicates that the provided route is invalid.
     */
    INVALID_ROUTE(106),

    /**
     * Indicates that the number of routes provided exceeds the allowed limit (more than 4).
     */
    TOO_MANY_ROUTES(107),

    /**
     * Indicates that the provided stop ID is invalid.
     */
    INVALID_STPID(108),

    /**
     * Indicates that the number of stop IDs provided exceeds the allowed limit (more than 4).
     */
    TOO_MANY_STPIDS(109),

    /**
     * Indicates that a non-integer value was specified for the maximum number of results.
     */
    INVALID_MAX(110),

    /**
     * Indicates that the provided maximum number of results is not a positive integer.
     */
    MAX_NOT_POSITIVE(111),

    /**
     * Indicates that the provided stop ID is not an integer.
     */
    STPID_NOT_INTEGER(112),

    /**
     * Indicates that the query string contains a parameter that is not recognized by the API. The supported API
     * parameters are "mapid", "key", "rt", "stpid", and "max".
     */
    INVALID_PARAMETER(500),

    /**
     * Indicates that the server encountered an unexpected error that prevented it from fulfilling the request.
     */
    SERVER_ERROR(900),

    /**
     * Indicates that an unknown error occurred that does not match any of the defined error codes.
     */
    UNKNOWN(-1);

    /**
     * The integer code associated with this error code.
     */
    private final int code;

    /**
     * Constructs an {@code ArrivalsErrorCode}.
     *
     * @param code the integer code associated with the error code
     */
    ArrivalsErrorCode(int code) {
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
     * Returns the {@code ArrivalsErrorCode} corresponding to the given integer code.
     *
     * @param code the integer code to look up
     * @return the corresponding {@code ArrivalsErrorCode}, or {@code UNKNOWN} if the code does not match any defined
     * error code
     */
    public static ArrivalsErrorCode fromCode(int code) {
        return switch (code) {
            case 0 -> OK;
            case 100 -> MISSING_PARAMETER;
            case 101 -> INVALID_API_KEY;
            case 102 -> DAILY_LIMIT_EXCEEDED;
            case 103 -> INVALID_MAPID;
            case 104 -> MAPID_NOT_INTEGER;
            case 105 -> TOO_MANY_MAPIDS;
            case 106 -> INVALID_ROUTE;
            case 107 -> TOO_MANY_ROUTES;
            case 108 -> INVALID_STPID;
            case 109 -> TOO_MANY_STPIDS;
            case 110 -> INVALID_MAX;
            case 111 -> MAX_NOT_POSITIVE;
            case 112 -> STPID_NOT_INTEGER;
            case 500 -> INVALID_PARAMETER;
            case 900 -> SERVER_ERROR;
            default -> UNKNOWN;
        };
    }
}
