package com.cta4j.alert.detailedalert.internal.impl;

import com.cta4j.alert.common.internal.config.AlertApiConfig;
import com.cta4j.alert.common.internal.util.AlertApiConstants;
import com.cta4j.alert.common.model.AlertTrainLine;
import com.cta4j.alert.detailedalert.DetailedAlertsApi;
import com.cta4j.alert.detailedalert.exception.Cta4jDetailedAlertsException;
import com.cta4j.alert.detailedalert.exception.DetailedAlertsErrorCode;
import com.cta4j.alert.detailedalert.internal.mapper.AlertMapper;
import com.cta4j.alert.detailedalert.internal.wire.CtaAlert;
import com.cta4j.alert.detailedalert.internal.wire.CtaAlerts;
import com.cta4j.alert.detailedalert.internal.wire.CtaDetailedAlertsResponse;
import com.cta4j.alert.detailedalert.model.Alert;
import com.cta4j.alert.detailedalert.query.AlertsQuery;
import com.cta4j.alert.detailedalert.query.BusRouteAlertsQuery;
import com.cta4j.alert.detailedalert.query.LineAlertsQuery;
import com.cta4j.alert.detailedalert.query.StationAlertsQuery;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class DetailedAlertsApiImpl implements DetailedAlertsApi {
    private final AlertApiConfig config;

    public DetailedAlertsApiImpl(AlertApiConfig config) {
        this.config = Objects.requireNonNull(config);
    }

    @Override
    public List<Alert> list(AlertsQuery query) {
        Objects.requireNonNull(query);

        String activeOnlyString = String.valueOf(query.activeOnly());
        String accessibilityString = String.valueOf(query.accessibility());
        String plannedString = String.valueOf(query.planned());

        URIBuilder builder = new URIBuilder()
            .setScheme(this.config.scheme())
            .setHost(this.config.host())
            .setPort(this.config.port())
            .setPath(AlertApiConstants.DETAILED_ALERTS_ENDPOINT)
            .addParameter("activeonly", activeOnlyString)
            .addParameter("accessibility", accessibilityString)
            .addParameter("planned", plannedString)
            .addParameter("outputType", "JSON");

        return this.makeRequest(builder, query.byStartDate(), query.recentDays());
    }

    @Override
    public List<Alert> findByBusRouteIds(BusRouteAlertsQuery query) {
        Objects.requireNonNull(query);

        List<String> routeIds = query.routeIds();

        if (routeIds.isEmpty()) {
            return List.of();
        }

        return this.makeRequest(
            query.activeOnly(),
            query.accessibility(),
            query.planned(),
            routeIds,
            "routeid",
            query.byStartDate(),
            query.recentDays()
        );
    }

    @Override
    public List<Alert> findByLines(LineAlertsQuery query) {
        Objects.requireNonNull(query);

        List<AlertTrainLine> lines = query.lines();

        if (lines.isEmpty()) {
            return List.of();
        }

        List<String> lineStrings = lines.stream()
                                        .map(AlertTrainLine::getCode)
                                        .toList();

        return this.makeRequest(
            query.activeOnly(),
            query.accessibility(),
            query.planned(),
            lineStrings,
            "routeid",
            query.byStartDate(),
            query.recentDays()
        );
    }

    @Override
    public List<Alert> findByStationIds(StationAlertsQuery query) {
        Objects.requireNonNull(query);

        List<String> stationIds = query.stationIds();

        if (stationIds.isEmpty()) {
            return List.of();
        }

        return this.makeRequest(
            query.activeOnly(),
            query.accessibility(),
            query.planned(),
            stationIds,
            "stationid",
            query.byStartDate(),
            query.recentDays()
        );
    }

    private List<Alert> makeRequest(
        boolean activeOnly,
        boolean accessibility,
        boolean planned,
        List<String> ids,
        String idsParameterName,
        @Nullable LocalDate byStartDate,
        @Nullable Integer recentDays
    ) {
        String activeOnlyString = String.valueOf(activeOnly);
        String accessibilityString = String.valueOf(accessibility);
        String plannedString = String.valueOf(planned);
        String idsString = String.join(",", ids);

        URIBuilder builder = new URIBuilder()
            .setScheme(this.config.scheme())
            .setHost(this.config.host())
            .setPort(this.config.port())
            .setPath(AlertApiConstants.DETAILED_ALERTS_ENDPOINT)
            .addParameter("activeonly", activeOnlyString)
            .addParameter("accessibility", accessibilityString)
            .addParameter("planned", plannedString)
            .addParameter(idsParameterName, idsString)
            .addParameter("outputType", "JSON");

        return this.makeRequest(builder, byStartDate, recentDays);
    }

    private List<Alert> makeRequest(
        URIBuilder builder,
        @Nullable LocalDate byStartDate,
        @Nullable Integer recentDays
    ) {
        if (byStartDate != null) {
            String byStartDateString = DateTimeFormatter.BASIC_ISO_DATE.format(byStartDate);

            builder.addParameter("bystartdate", byStartDateString);
        }

        if (recentDays != null) {
            String recentDaysString = String.valueOf(recentDays);

            builder.addParameter("recentdays", recentDaysString);
        }

        String url = builder.toString();

        String response;

        try {
            response = Request.get(url)
                              .execute()
                              .returnContent()
                              .asString();
        } catch (IOException e) {
            String message = Objects.requireNonNullElse(e.getMessage(), "Request failed");

            throw new Cta4jDetailedAlertsException(message, e);
        }

        CtaDetailedAlertsResponse detailedAlertsResponse;

        try {
            detailedAlertsResponse = JsonMapper.shared()
                                               .readValue(response, CtaDetailedAlertsResponse.class);
        } catch (JacksonException e) {
            throw new Cta4jDetailedAlertsException("Failed to parse response", e);
        }

        CtaAlerts ctaAlerts = detailedAlertsResponse.ctaAlerts();

        List<CtaAlert> alert = ctaAlerts.alert();

        if (alert != null && !alert.isEmpty()) {
            return alert.stream()
                        .map(AlertMapper.INSTANCE::toDomain)
                        .toList();
        }

        String errorCodeString = ctaAlerts.errorCode();

        int integerCode;

        try {
            integerCode = Integer.parseInt(errorCodeString);
        } catch (NumberFormatException e) {
            throw new Cta4jDetailedAlertsException("Failed to parse error code", e);
        }

        DetailedAlertsErrorCode errorCode = DetailedAlertsErrorCode.fromCode(integerCode);

        if (errorCode == DetailedAlertsErrorCode.OK
            || errorCode == DetailedAlertsErrorCode.NO_ACTIVE_ALERTS
            || errorCode == DetailedAlertsErrorCode.NO_ACTIVE_ALERTS_FOR_FILTER) {
            return List.of();
        }

        String errorMessage = ctaAlerts.errorMessage();

        String message = errorMessage == null || errorMessage.isBlank()
            ? "An unknown error occurred."
            : errorMessage;

        throw new Cta4jDetailedAlertsException(message, integerCode);
    }
}
