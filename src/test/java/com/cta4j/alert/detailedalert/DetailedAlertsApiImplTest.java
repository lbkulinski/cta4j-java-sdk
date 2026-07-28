package com.cta4j.alert.detailedalert;

import com.cta4j.TestFixtures;
import com.cta4j.alert.common.internal.config.AlertApiConfig;
import com.cta4j.alert.common.internal.util.AlertApiConstants;
import com.cta4j.alert.common.model.AlertTrainLine;
import com.cta4j.alert.detailedalert.exception.Cta4jDetailedAlertsException;
import com.cta4j.alert.detailedalert.exception.DetailedAlertsErrorCode;
import com.cta4j.alert.detailedalert.internal.impl.DetailedAlertsApiImpl;
import com.cta4j.alert.detailedalert.model.Alert;
import com.cta4j.alert.detailedalert.query.AlertsQuery;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.core.JacksonException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.*;

class DetailedAlertsApiImplTest {
    private WireMockServer server;
    private DetailedAlertsApiImpl api;

    @BeforeEach
    void setUp() {
        this.server = new WireMockServer(wireMockConfig().dynamicPort());
        this.server.start();
        AlertApiConfig config = new AlertApiConfig("http", "localhost", this.server.port());
        this.api = new DetailedAlertsApiImpl(config);
    }

    @AfterEach
    void tearDown() {
        this.server.stop();
    }

    @Test
    void list_returnsAlerts_whenResponseContainsData() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/alerts.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/detailedalert/list_success.json"))));

        List<Alert> alerts = this.api.list();

        assertThat(alerts).hasSize(3);

        Alert busReroute = alerts.getFirst();
        assertThat(busReroute.id()).isEqualTo("115070");
        assertThat(busReroute.headline()).isEqualTo("Route 22 Rerouted");
        assertThat(busReroute.severity().score()).isEqualTo(37);
        assertThat(busReroute.endTime()).isNotNull();
        assertThat(busReroute.openEnded()).isFalse();
        assertThat(busReroute.major()).isFalse();
        assertThat(busReroute.impactedServices()).hasSize(1);
        assertThat(busReroute.ttim()).isEqualTo("0");
        assertThat(busReroute.guid()).isEqualTo("664b81c1-197b-450b-a00c-090483b90bb9");

        Alert stationClosure = alerts.get(1);
        assertThat(stationClosure.id()).isEqualTo("115080");
        assertThat(stationClosure.endTime()).isNull();
        assertThat(stationClosure.openEnded()).isTrue();
        assertThat(stationClosure.major()).isFalse();
        assertThat(stationClosure.impactedServices()).hasSize(2);
        assertThat(stationClosure.ttim()).isEqualTo("1");
        assertThat(stationClosure.guid()).isEqualTo("d41b2532-09ca-4827-b9c6-f4299cc86fb6");

        // Confirmed against the live API: EventStart/EventEnd are sometimes bare dates with no time-of-day.
        Alert dateOnlyAlert = alerts.get(2);
        assertThat(dateOnlyAlert.id()).isEqualTo("115090");
        assertThat(dateOnlyAlert.startTime()).isNotNull();
        assertThat(dateOnlyAlert.endTime()).isNotNull();
    }

    @Test
    void list_sendsDefaultQueryParameters() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/alerts.aspx"))
            .withQueryParam("activeonly", equalTo("false"))
            .withQueryParam("accessibility", equalTo("true"))
            .withQueryParam("planned", equalTo("true"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/detailedalert/list_success.json"))));

        List<Alert> alerts = this.api.list();

        assertThat(alerts).hasSize(3);
        this.server.verify(getRequestedFor(urlPathEqualTo("/api/1.0/alerts.aspx"))
            .withoutQueryParam("bystartdate")
            .withoutQueryParam("recentdays"));
    }

    @Test
    void list_sendsByStartDateParameter_whenProvided() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/alerts.aspx"))
            .withQueryParam("bystartdate", equalTo("20260701"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/detailedalert/list_success.json"))));

        AlertsQuery query = AlertsQuery.builder()
                                       .byStartDate(LocalDate.of(2026, 7, 1))
                                       .build();

        List<Alert> alerts = this.api.list(query);

        assertThat(alerts).hasSize(3);
    }

    @Test
    void list_sendsRecentDaysParameter_whenProvided() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/alerts.aspx"))
            .withQueryParam("recentdays", equalTo("7"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/detailedalert/list_success.json"))));

        AlertsQuery query = AlertsQuery.builder()
                                       .recentDays(7)
                                       .build();

        List<Alert> alerts = this.api.list(query);

        assertThat(alerts).hasSize(3);
    }

    @Test
    void list_returnsEmpty_whenErrorCodeIsNoActiveAlerts() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/alerts.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/detailedalert/no_active_alerts.json"))));

        List<Alert> alerts = this.api.list();

        assertThat(alerts).isEmpty();
    }

    @Test
    void list_returnsEmpty_whenErrorCodeIsNoActiveAlertsForFilter() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/alerts.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/detailedalert/no_active_alerts_for_filter.json"))));

        List<Alert> alerts = this.api.list();

        assertThat(alerts).isEmpty();
    }

    @Test
    void list_returnsEmpty_whenAlertIsExplicitlyEmptyArray() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/alerts.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/detailedalert/empty_alert_array.json"))));

        List<Alert> alerts = this.api.list();

        assertThat(alerts).isEmpty();
    }

    @Test
    void list_returnsEmpty_whenErrorCodeIsOk_andNoAlertData() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/alerts.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/detailedalert/ok_no_alerts.json"))));

        List<Alert> alerts = this.api.list();

        assertThat(alerts).isEmpty();
    }

    @Test
    void list_throwsCta4jDetailedAlertsException_whenResponseContainsFatalError() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/alerts.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/detailedalert/fatal_error.json"))));

        assertThatThrownBy(() -> this.api.list())
            .isInstanceOf(Cta4jDetailedAlertsException.class)
            .hasMessage("Invalid option for parameter 'activeonly': Valid options are 'true', 'false'")
            .satisfies(e -> assertThat(((Cta4jDetailedAlertsException) e).getErrorCode())
                .isEqualTo(DetailedAlertsErrorCode.INVALID_ACTIVEONLY))
            .satisfies(e -> assertThat(((Cta4jDetailedAlertsException) e).getRawErrorCode()).isEqualTo(100))
            .satisfies(e -> assertThat(((Cta4jDetailedAlertsException) e).getEndpoint())
                .isEqualTo(AlertApiConstants.DETAILED_ALERTS_ENDPOINT));
    }

    @Test
    void list_throwsCta4jDetailedAlertsException_withDefaultMessage_whenErrorMessageIsBlank() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/alerts.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/detailedalert/error_message_blank.json"))));

        assertThatThrownBy(() -> this.api.list())
            .isInstanceOf(Cta4jDetailedAlertsException.class)
            .hasMessage("An unknown error occurred.")
            .satisfies(e -> assertThat(((Cta4jDetailedAlertsException) e).getErrorCode())
                .isEqualTo(DetailedAlertsErrorCode.SERVER_ERROR));
    }

    @Test
    void list_throwsCta4jDetailedAlertsException_withDefaultMessage_whenErrorMessageIsAbsent() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/alerts.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/detailedalert/error_message_absent.json"))));

        assertThatThrownBy(() -> this.api.list())
            .isInstanceOf(Cta4jDetailedAlertsException.class)
            .hasMessage("An unknown error occurred.")
            .satisfies(e -> assertThat(((Cta4jDetailedAlertsException) e).getErrorCode())
                .isEqualTo(DetailedAlertsErrorCode.INVALID_PARAMETER));
    }

    @Test
    void list_throwsCta4jDetailedAlertsException_whenErrorCodeIsNotNumeric() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/alerts.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/detailedalert/bad_error_code.json"))));

        assertThatThrownBy(() -> this.api.list())
            .isInstanceOf(Cta4jDetailedAlertsException.class)
            .hasMessage("Failed to parse error code")
            .satisfies(e -> assertThat(e.getCause()).isInstanceOf(NumberFormatException.class));
    }

    @Test
    void list_throwsCta4jDetailedAlertsException_whenResponseIsNotJson() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/alerts.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("not-json")));

        assertThatThrownBy(() -> this.api.list())
            .isInstanceOf(Cta4jDetailedAlertsException.class)
            .hasMessage("Failed to parse response")
            .satisfies(e -> assertThat(((Cta4jDetailedAlertsException) e).getEndpoint())
                .isEqualTo(AlertApiConstants.DETAILED_ALERTS_ENDPOINT))
            .satisfies(e -> assertThat(e.getCause()).isInstanceOf(JacksonException.class));
    }

    @Test
    void list_throwsCta4jDetailedAlertsException_whenServerReturnsErrorStatus() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/alerts.aspx"))
            .willReturn(aResponse()
                .withStatus(500)));

        assertThatThrownBy(() -> this.api.list())
            .isInstanceOf(Cta4jDetailedAlertsException.class)
            .hasMessageContaining("500")
            .satisfies(e -> assertThat(e.getCause()).isNotNull());
    }

    @Test
    void findByBusRouteIds_returnsEmpty_whenInputIsEmpty() {
        List<Alert> alerts = this.api.findByBusRouteIds(List.of());

        assertThat(alerts).isEmpty();
        this.server.verify(0, anyRequestedFor(anyUrl()));
    }

    @Test
    void findByBusRouteIds_sendsRouteidParameter_asCommaJoined() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/alerts.aspx"))
            .withQueryParam("routeid", equalTo("22,53"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/detailedalert/list_success.json"))));

        List<Alert> alerts = this.api.findByBusRouteIds(List.of("22", "53"));

        assertThat(alerts).hasSize(3);
    }

    @Test
    void findByBusRouteIds_throwsIllegalArgumentException_whenRouteIdIsTrainLine() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.api.findByBusRouteIds(List.of("Red")))
            .withMessageContaining("Red is a train line, not a bus route");

        this.server.verify(0, anyRequestedFor(anyUrl()));
    }

    @Test
    void findByBusRouteIds_throwsIllegalArgumentException_whenRouteIdIsTrainLine_caseInsensitive() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.api.findByBusRouteIds(List.of("red")));

        this.server.verify(0, anyRequestedFor(anyUrl()));
    }

    @Test
    void findByBusRouteIds_throwsNullPointerException_whenRouteIdsContainsNull() {
        List<String> withNull = Arrays.asList("22", null);

        assertThatNullPointerException().isThrownBy(() -> this.api.findByBusRouteIds(withNull));
    }

    @Test
    void findByBusRouteId_delegatesToFindByBusRouteIds() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/alerts.aspx"))
            .withQueryParam("routeid", equalTo("22"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/detailedalert/list_success.json"))));

        List<Alert> alerts = this.api.findByBusRouteId("22");

        assertThat(alerts).hasSize(3);
    }

    @Test
    void findByLines_returnsEmpty_whenInputIsEmpty() {
        List<Alert> alerts = this.api.findByLines(List.of());

        assertThat(alerts).isEmpty();
        this.server.verify(0, anyRequestedFor(anyUrl()));
    }

    @Test
    void findByLines_sendsRouteidParameter_asCommaJoinedCodes() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/alerts.aspx"))
            .withQueryParam("routeid", equalTo("Red,Blue"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/detailedalert/list_success.json"))));

        List<Alert> alerts = this.api.findByLines(List.of(AlertTrainLine.RED, AlertTrainLine.BLUE));

        assertThat(alerts).hasSize(3);
    }

    @Test
    void findByLines_sendsRouteidParameter_usingPurpleExpressCode() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/alerts.aspx"))
            .withQueryParam("routeid", equalTo("Pexp"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/detailedalert/list_success.json"))));

        List<Alert> alerts = this.api.findByLine(AlertTrainLine.PURPLE_EXPRESS);

        assertThat(alerts).hasSize(3);
    }

    @Test
    void findByLines_throwsNullPointerException_whenLinesContainsNull() {
        List<AlertTrainLine> withNull = Arrays.asList(AlertTrainLine.RED, null);

        assertThatNullPointerException().isThrownBy(() -> this.api.findByLines(withNull));
    }

    @Test
    void findByLine_delegatesToFindByLines() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/alerts.aspx"))
            .withQueryParam("routeid", equalTo("Red"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/detailedalert/list_success.json"))));

        List<Alert> alerts = this.api.findByLine(AlertTrainLine.RED);

        assertThat(alerts).hasSize(3);
    }

    @Test
    void findByStationIds_returnsEmpty_whenInputIsEmpty() {
        List<Alert> alerts = this.api.findByStationIds(List.of());

        assertThat(alerts).isEmpty();
        this.server.verify(0, anyRequestedFor(anyUrl()));
    }

    @Test
    void findByStationIds_sendsStationidParameter_asCommaJoined() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/alerts.aspx"))
            .withQueryParam("stationid", equalTo("40380,41260"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/detailedalert/list_success.json"))));

        List<Alert> alerts = this.api.findByStationIds(List.of("40380", "41260"));

        assertThat(alerts).hasSize(3);
    }

    @Test
    void findByStationIds_throwsNullPointerException_whenStationIdsContainsNull() {
        List<String> withNull = Arrays.asList("40380", null);

        assertThatNullPointerException().isThrownBy(() -> this.api.findByStationIds(withNull));
    }

    @Test
    void findByStationId_delegatesToFindByStationIds() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/alerts.aspx"))
            .withQueryParam("stationid", equalTo("41260"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/detailedalert/list_success.json"))));

        List<Alert> alerts = this.api.findByStationId("41260");

        assertThat(alerts).hasSize(3);
    }
}
