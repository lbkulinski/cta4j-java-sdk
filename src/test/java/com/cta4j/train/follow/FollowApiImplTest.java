package com.cta4j.train.follow;

import com.cta4j.TestFixtures;
import com.cta4j.exception.Cta4jException;
import com.cta4j.train.common.internal.config.TrainApiConfig;
import com.cta4j.train.follow.internal.impl.FollowApiImpl;
import com.cta4j.train.follow.model.FollowTrain;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.*;

class FollowApiImplTest {
    private WireMockServer server;
    private FollowApiImpl api;

    @BeforeEach
    void setUp() {
        this.server = new WireMockServer(wireMockConfig().dynamicPort());
        this.server.start();
        TrainApiConfig config = new TrainApiConfig(
            "http",
            "localhost",
            this.server.port(),
            "http://localhost:" + this.server.port() + "/stations",
            "testkey"
        );
        this.api = new FollowApiImpl(config);
    }

    @AfterEach
    void tearDown() {
        this.server.stop();
    }

    @Test
    void findByRun_returnsFollowTrain_whenResponseContainsData() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttfollow.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/follow/success.json"))));

        Optional<FollowTrain> result = this.api.findByRun("123");

        assertThat(result).isPresent();
        FollowTrain followTrain = result.get();
        assertThat(followTrain.arrivals()).hasSize(1);
        assertThat(followTrain.coordinates()).isNotNull();
    }

    @Test
    void findByRun_returnsFollowTrainWithNullCoordinates_whenResponseHasNoPosition() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttfollow.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/follow/no_position.json"))));

        Optional<FollowTrain> result = this.api.findByRun("123");

        assertThat(result).isPresent();
        FollowTrain followTrain = result.get();
        assertThat(followTrain.arrivals()).hasSize(1);
        assertThat(followTrain.coordinates()).isNull();
    }

    @Test
    void findByRun_returnsEmpty_whenResponseIsNotFound() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttfollow.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/follow/not_found.json"))));

        Optional<FollowTrain> result = this.api.findByRun("9999");

        assertThat(result).isEmpty();
    }

    @Test
    void findByRun_throwsCta4jException_whenResponseContainsError() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttfollow.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/follow/error.json"))));

        assertThatThrownBy(() -> this.api.findByRun("123"))
            .isInstanceOf(Cta4jException.class);
    }

    @Test
    void findByRun_throwsCta4jException_whenResponseIsNotJson() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttfollow.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("not-json")));

        assertThatThrownBy(() -> this.api.findByRun("123"))
            .isInstanceOf(Cta4jException.class);
    }

    @Test
    void findByRun_sendsRunnumberParameter() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttfollow.aspx"))
            .withQueryParam("runnumber", equalTo("123"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/follow/success.json"))));

        Optional<FollowTrain> result = this.api.findByRun("123");

        assertThat(result).isPresent();
    }

    @Test
    void findByRun_throwsCta4jException_whenErrCdIsNotNumeric() {
        this.server.stubFor(get(urlPathEqualTo("/api/1.0/ttfollow.aspx"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("train/follow/invalid_err_cd.json"))));

        assertThatThrownBy(() -> this.api.findByRun("123"))
            .isInstanceOf(Cta4jException.class);
    }
}
