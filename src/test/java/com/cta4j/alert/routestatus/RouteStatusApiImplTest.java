package com.cta4j.alert.routestatus;

import com.cta4j.TestFixtures;
import com.cta4j.alert.common.internal.config.AlertApiConfig;
import com.cta4j.alert.common.internal.util.AlertApiConstants;
import com.cta4j.alert.routestatus.exception.Cta4jRouteStatusException;
import com.cta4j.alert.routestatus.exception.RouteStatusErrorCode;
import com.cta4j.alert.routestatus.internal.impl.RouteStatusApiImpl;
import com.cta4j.alert.routestatus.model.RouteStatus;
import com.cta4j.alert.routestatus.model.ServiceType;
import com.cta4j.common.train.TrainLine;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.core.JacksonException;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.*;

class RouteStatusApiImplTest {
    private WireMockServer server;
    private RouteStatusApiImpl api;

    @BeforeEach
    void setUp() {
        this.server = new WireMockServer(wireMockConfig().dynamicPort());
        this.server.start();
        AlertApiConfig config = new AlertApiConfig("http", "localhost", this.server.port());
        this.api = new RouteStatusApiImpl(config);
    }

    @AfterEach
    void tearDown() {
        this.server.stop();
    }

    @Test
    void list_returnsRouteStatuses_whenResponseContainsData() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/routes.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/routestatus/list_success.json"))));

        List<RouteStatus> statuses = this.api.list();

        assertThat(statuses).hasSize(3);
        RouteStatus redLine = statuses.getFirst();
        assertThat(redLine.route()).isEqualTo("Red Line");
        assertThat(redLine.color()).isEqualTo("c60c30");
        assertThat(redLine.textColor()).isEqualTo("ffffff");
        assertThat(redLine.serviceId()).isEqualTo("Red");
        assertThat(redLine.url()).hasToString("http://www.transitchicago.com/redline/");
        assertThat(redLine.status()).isEqualTo("Normal Service");
        assertThat(redLine.statusColor()).isEqualTo("404040");
    }

    @Test
    void list_returnsEmpty_whenErrorCodeIsExplicitlyEmptyArray() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/routes.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/routestatus/error_code_empty_array.json"))));

        List<RouteStatus> statuses = this.api.list();

        assertThat(statuses).isEmpty();
    }

    @Test
    void list_returnsEmpty_whenNoRouteInfoAndNoErrorCode() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/routes.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/routestatus/no_data_no_error.json"))));

        List<RouteStatus> statuses = this.api.list();

        assertThat(statuses).isEmpty();
    }

    @Test
    void list_returnsEmpty_whenRouteInfoIsExplicitlyEmptyArray() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/routes.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/routestatus/empty_route_info_array.json"))));

        List<RouteStatus> statuses = this.api.list();

        assertThat(statuses).isEmpty();
    }

    @Test
    void list_returnsEmpty_whenErrorCodeIsNoResults() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/routes.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/routestatus/no_results.json"))));

        List<RouteStatus> statuses = this.api.list();

        assertThat(statuses).isEmpty();
    }

    @Test
    void list_returnsEmpty_whenErrorCodesAreDistinct_usingFirstValue() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/routes.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/routestatus/distinct_error_codes.json"))));

        List<RouteStatus> statuses = this.api.list();

        assertThat(statuses).isEmpty();
    }

    @Test
    void list_throwsCta4jRouteStatusException_whenResponseContainsFatalError() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/routes.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/routestatus/invalid_type_error.json"))));

        assertThatThrownBy(() -> this.api.list())
            .isInstanceOf(Cta4jRouteStatusException.class)
            .hasMessage("Invalid option for parameter 'type': Valid options are 'bus', 'rail', 'station' or 'systemwide'")
            .satisfies(e -> assertThat(((Cta4jRouteStatusException) e).getErrorCode())
                .isEqualTo(RouteStatusErrorCode.INVALID_TYPE))
            .satisfies(e -> assertThat(((Cta4jRouteStatusException) e).getRawErrorCode()).isEqualTo(101))
            .satisfies(e -> assertThat(((Cta4jRouteStatusException) e).getEndpoint())
                .isEqualTo(AlertApiConstants.ROUTE_STATUS_ENDPOINT));
    }

    @Test
    void list_throwsCta4jRouteStatusException_withDefaultMessage_whenErrorMessageIsExplicitlyEmptyArray() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/routes.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/routestatus/error_message_empty_array.json"))));

        assertThatThrownBy(() -> this.api.list())
            .isInstanceOf(Cta4jRouteStatusException.class)
            .hasMessage("An unknown error occurred.")
            .satisfies(e -> assertThat(((Cta4jRouteStatusException) e).getErrorCode())
                .isEqualTo(RouteStatusErrorCode.INVALID_TYPE));
    }

    @Test
    void list_throwsCta4jRouteStatusException_withDefaultMessage_whenErrorMessageIsBlank() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/routes.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/routestatus/blank_error_message.json"))));

        assertThatThrownBy(() -> this.api.list())
            .isInstanceOf(Cta4jRouteStatusException.class)
            .hasMessage("An unknown error occurred.")
            .satisfies(e -> assertThat(((Cta4jRouteStatusException) e).getErrorCode())
                .isEqualTo(RouteStatusErrorCode.SERVER_ERROR));
    }

    @Test
    void list_throwsCta4jRouteStatusException_withDefaultMessage_whenErrorMessageElementIsNull() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/routes.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/routestatus/error_null_message_element.json"))));

        assertThatThrownBy(() -> this.api.list())
            .isInstanceOf(Cta4jRouteStatusException.class)
            .hasMessage("An unknown error occurred.")
            .satisfies(e -> assertThat(((Cta4jRouteStatusException) e).getErrorCode())
                .isEqualTo(RouteStatusErrorCode.INVALID_TYPE));
    }

    @Test
    void list_throwsCta4jRouteStatusException_withDefaultMessage_whenErrorMessageIsAbsent() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/routes.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/routestatus/error_no_message.json"))));

        assertThatThrownBy(() -> this.api.list())
            .isInstanceOf(Cta4jRouteStatusException.class)
            .hasMessage("An unknown error occurred.")
            .satisfies(e -> assertThat(((Cta4jRouteStatusException) e).getErrorCode())
                .isEqualTo(RouteStatusErrorCode.INVALID_TYPE));
    }

    @Test
    void list_throwsCta4jRouteStatusException_whenErrorCodeIsNotNumeric() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/routes.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/routestatus/bad_error_code.json"))));

        assertThatThrownBy(() -> this.api.list())
            .isInstanceOf(Cta4jRouteStatusException.class)
            .hasMessage("Failed to parse error code")
            .satisfies(e -> assertThat(e.getCause()).isInstanceOf(NumberFormatException.class));
    }

    @Test
    void list_throwsCta4jRouteStatusException_whenResponseIsNotJson() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/routes.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("not-json")));

        assertThatThrownBy(() -> this.api.list())
            .isInstanceOf(Cta4jRouteStatusException.class)
            .hasMessage("Failed to parse response")
            .satisfies(e -> assertThat(((Cta4jRouteStatusException) e).getEndpoint())
                .isEqualTo(AlertApiConstants.ROUTE_STATUS_ENDPOINT))
            .satisfies(e -> assertThat(e.getCause()).isInstanceOf(JacksonException.class));
    }

    @Test
    void list_throwsCta4jRouteStatusException_whenServerReturnsErrorStatus() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/routes.aspx"))
            .willReturn(aResponse()
                .withStatus(500)));

        assertThatThrownBy(() -> this.api.list())
            .isInstanceOf(Cta4jRouteStatusException.class)
            .hasMessageContaining("500")
            .satisfies(e -> assertThat(e.getCause()).isNotNull());
    }

    @Test
    void findByTypes_returnsEmpty_whenInputIsEmpty() {
        List<RouteStatus> statuses = this.api.findByTypes(List.of());

        assertThat(statuses).isEmpty();
        this.server.verify(0, anyRequestedFor(anyUrl()));
    }

    @Test
    void findByTypes_sendsTypeParameter_asCommaJoinedLowercase() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/routes.aspx"))
            .withQueryParam("type", equalTo("bus,rail"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/routestatus/list_success.json"))));

        List<RouteStatus> statuses = this.api.findByTypes(List.of(ServiceType.BUS, ServiceType.RAIL));

        assertThat(statuses).hasSize(3);
    }

    @Test
    void findByType_delegatesToFindByTypes() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/routes.aspx"))
            .withQueryParam("type", equalTo("rail"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/routestatus/rail_success.json"))));

        List<RouteStatus> statuses = this.api.findByType(ServiceType.RAIL);

        assertThat(statuses).hasSize(2);
    }

    @Test
    void findByBusRouteIds_returnsEmpty_whenInputIsEmpty() {
        List<RouteStatus> statuses = this.api.findByBusRouteIds(List.of());

        assertThat(statuses).isEmpty();
        this.server.verify(0, anyRequestedFor(anyUrl()));
    }

    @Test
    void findByBusRouteIds_sendsRouteidParameter_asCommaJoined() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/routes.aspx"))
            .withQueryParam("routeid", equalTo("22,53"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/routestatus/bus_success.json"))));

        List<RouteStatus> statuses = this.api.findByBusRouteIds(List.of("22", "53"));

        assertThat(statuses).hasSize(1);
    }

    @Test
    void findByBusRouteIds_returnsRouteStatuses_whenResponseOmitsErrorEnvelope() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/routes.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/routestatus/bus_success.json"))));

        List<RouteStatus> statuses = this.api.findByBusRouteIds(List.of("22"));

        assertThat(statuses).hasSize(1);
        RouteStatus status = statuses.getFirst();
        assertThat(status.serviceId()).isEqualTo("22");
        assertThat(status.status()).isEqualTo("Bus Stop Note");
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
    void findByBusRouteId_delegatesToFindByBusRouteIds() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/routes.aspx"))
            .withQueryParam("routeid", equalTo("22"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/routestatus/bus_success.json"))));

        List<RouteStatus> statuses = this.api.findByBusRouteId("22");

        assertThat(statuses).hasSize(1);
    }

    @Test
    void findByLines_returnsEmpty_whenInputIsEmpty() {
        List<RouteStatus> statuses = this.api.findByLines(List.of());

        assertThat(statuses).isEmpty();
        this.server.verify(0, anyRequestedFor(anyUrl()));
    }

    @Test
    void findByLines_sendsRouteidParameter_asCommaJoinedCodes() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/routes.aspx"))
            .withQueryParam("routeid", equalTo("Red,Blue"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/routestatus/rail_success.json"))));

        List<RouteStatus> statuses = this.api.findByLines(List.of(TrainLine.RED, TrainLine.BLUE));

        assertThat(statuses).hasSize(2);
    }

    @Test
    void findByLines_returnsRouteStatuses_whenResponseContainsData() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/routes.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/routestatus/rail_success.json"))));

        List<RouteStatus> statuses = this.api.findByLines(List.of(TrainLine.RED, TrainLine.BLUE));

        assertThat(statuses).hasSize(2);
        RouteStatus redLine = statuses.getFirst();
        assertThat(redLine.serviceId()).isEqualTo("Red");
        assertThat(redLine.route()).isEqualTo("Red Line");
        assertThat(redLine.status()).isEqualTo("Normal Service");
    }

    @Test
    void findByLine_delegatesToFindByLines() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/routes.aspx"))
            .withQueryParam("routeid", equalTo("Red"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/routestatus/rail_success.json"))));

        List<RouteStatus> statuses = this.api.findByLine(TrainLine.RED);

        assertThat(statuses).hasSize(2);
    }

    @Test
    void findByStationId_sendsStationidParameter() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/routes.aspx"))
            .withQueryParam("stationid", equalTo("40380"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/routestatus/rail_success.json"))));

        List<RouteStatus> statuses = this.api.findByStationId("40380");

        assertThat(statuses).hasSize(2);
    }

    @Test
    void findByStationId_returnsRouteStatuses_whenResponseContainsData() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/routes.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("alert/routestatus/bus_success.json"))));

        List<RouteStatus> statuses = this.api.findByStationId("40380");

        assertThat(statuses).hasSize(1);
        assertThat(statuses.getFirst().serviceId()).isEqualTo("22");
    }
}
