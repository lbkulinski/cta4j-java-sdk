package com.cta4j.alert.detailedalert.exception;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/// Represents the error codes returned by the CTA Detailed Alerts API.
@NullMarked
public enum DetailedAlertsErrorCode {
    /// Indicates that the request was successful and there were no errors.
    OK(0),

    /// Indicates that there are no active alerts.
    NO_ACTIVE_ALERTS(25),

    /// Indicates that there are no active alerts based on the provided filter criteria.
    NO_ACTIVE_ALERTS_FOR_FILTER(50),

    /// Indicates that the provided `activeonly` value is invalid.
    INVALID_ACTIVEONLY(100),

    /// Indicates that the provided `accessibility` value is invalid.
    INVALID_ACCESSIBILITY(101),

    /// Indicates that the provided `planned` value is invalid.
    INVALID_PLANNED(102),

    /// Indicates that the provided station ID is not an integer.
    STATIONID_NOT_INTEGER(103),

    /// Indicates that the provided `bystartdate` value is not a valid date in `yyyyMMdd` format.
    INVALID_BYSTARTDATE(104),

    /// Indicates that the provided `recentdays` value is not an integer.
    RECENTDAYS_NOT_INTEGER(105),

    /// Indicates that the `routeid` and `stationid` parameters were both provided, which is not allowed.
    ROUTEID_STATIONID_CONFLICT(106),

    /// Indicates that the `recentdays` and `bystartdate` parameters were both provided, which is not allowed.
    RECENTDAYS_BYSTARTDATE_CONFLICT(107),

    /// Indicates that the query string contains a parameter that is not recognized by the API. The supported API
    /// parameters are `activeonly`, `accessibility`, `planned`, `routeid`, `stationid`, `bystartdate`, `recentdays`,
    /// and `outputType`.
    INVALID_PARAMETER(500),

    /// Indicates that the server encountered an unexpected error that prevented it from fulfilling the request.
    SERVER_ERROR(900);

    private final int code;

    DetailedAlertsErrorCode(int code) {
        this.code = code;
    }

    /// Returns the integer code associated with this error code.
    ///
    /// @return the integer code
    public int getCode() {
        return this.code;
    }

    /// Returns the `DetailedAlertsErrorCode` corresponding to the given integer code.
    ///
    /// @param code the integer code to look up
    /// @return the corresponding `DetailedAlertsErrorCode`, or `null` if the code does not match any defined error
    /// code
    public static @Nullable DetailedAlertsErrorCode fromCode(int code) {
        return switch (code) {
            case 0 -> OK;
            case 25 -> NO_ACTIVE_ALERTS;
            case 50 -> NO_ACTIVE_ALERTS_FOR_FILTER;
            case 100 -> INVALID_ACTIVEONLY;
            case 101 -> INVALID_ACCESSIBILITY;
            case 102 -> INVALID_PLANNED;
            case 103 -> STATIONID_NOT_INTEGER;
            case 104 -> INVALID_BYSTARTDATE;
            case 105 -> RECENTDAYS_NOT_INTEGER;
            case 106 -> ROUTEID_STATIONID_CONFLICT;
            case 107 -> RECENTDAYS_BYSTARTDATE_CONFLICT;
            case 500 -> INVALID_PARAMETER;
            case 900 -> SERVER_ERROR;
            default -> null;
        };
    }
}
