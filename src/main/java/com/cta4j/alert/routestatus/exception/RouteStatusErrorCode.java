package com.cta4j.alert.routestatus.exception;

import org.jspecify.annotations.NullMarked;

/**
 * Represents the error codes returned by the CTA Route Status API.
 */
@NullMarked
public enum RouteStatusErrorCode {
    /**
     * Indicates that the request was successful and there were no errors.
     */
    OK(0),

    /**
     * Indicates that no routes or stations matched the provided filter criteria.
     * <p>
     * This code is not documented in the CTA Alerts API documentation for the Route Status API, but has been
     * observed in practice.
     */
    NO_RESULTS(50),

    /**
     * Indicates that the provided station ID is not an integer.
     */
    STATIONID_NOT_INTEGER(100),

    /**
     * Indicates that the provided service type is invalid.
     */
    INVALID_TYPE(101),

    /**
     * Indicates that the "routeid" and "stationid" parameters were both provided, which is not allowed.
     */
    ROUTEID_STATIONID_CONFLICT(102),

    /**
     * Indicates that the "routeid" and "type" parameters were both provided, which is not allowed.
     */
    ROUTEID_TYPE_CONFLICT(103),

    /**
     * Indicates that the "stationid" and "type" parameters were both provided, which is not allowed.
     */
    STATIONID_TYPE_CONFLICT(104),

    /**
     * Indicates that the query string contains a parameter that is not recognized by the API. The supported API
     * parameters are "type", "routeid", "stationid", and "outputType".
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
     * Constructs a {@code RouteStatusErrorCode}.
     *
     * @param code the integer code associated with the error code
     */
    RouteStatusErrorCode(int code) {
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
     * Returns the {@code RouteStatusErrorCode} corresponding to the given integer code.
     *
     * @param code the integer code to look up
     * @return the corresponding {@code RouteStatusErrorCode}, or {@code UNKNOWN} if the code does not match any
     * defined error code
     */
    public static RouteStatusErrorCode fromCode(int code) {
        return switch (code) {
            case 0 -> OK;
            case 50 -> NO_RESULTS;
            case 100 -> STATIONID_NOT_INTEGER;
            case 101 -> INVALID_TYPE;
            case 102 -> ROUTEID_STATIONID_CONFLICT;
            case 103 -> ROUTEID_TYPE_CONFLICT;
            case 104 -> STATIONID_TYPE_CONFLICT;
            case 500 -> INVALID_PARAMETER;
            case 900 -> SERVER_ERROR;
            default -> UNKNOWN;
        };
    }
}
