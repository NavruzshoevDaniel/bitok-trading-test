package com.bitoktraidingtest.component;

import com.bitoktraidingtest.BaseIntegrationTest;
import com.bitoktraidingtest.controller.InvestController;
import com.bitoktraidingtest.entity.UserInvestEntity;
import com.bitoktraidingtest.repository.UserInvestEntityRepository;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Sql(value = "/integration/db/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
class InvestControllerComponentTestV1 extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserInvestEntityRepository userInvestEntityRepository;

    @Test
    void shouldInvestTest() {
        //Given
        final String response = """
                {
                "shouldInvest": true,
                "message": "Фиксируем прибыль!"
                }
                """;
        stubFor(
                WireMock.get(urlEqualTo("/coins/bitcoin/history?date=04-02-2023"))
                        .willReturn(
                                aResponse()
                                        .withStatus(HttpStatus.OK.value())
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBodyFile("historicalBtcPrice.json")
                        )
        );
        stubFor(
                WireMock.get(urlEqualTo("/simple/price?vs_currencies=usd&ids=bitcoin"))
                        .willReturn(
                                aResponse()
                                        .withStatus(HttpStatus.OK.value())
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBodyFile("currentPrice.json")
                        )
        );
        //When & Then
        webTestClient.get()
                .uri(InvestController.SHOULD_INVEST_PATH)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json(response);
    }

    @Test
    void investTest() {
        //Given
        final String request = """
                {
                    "userId": "1",
                    "amount": 504.5
                }
                """;
        final String response = """
                {
                    "id":1,
                    "userId": "1",
                    "amount":504.5
                }
                """;

        //When & Then
        webTestClient.post()
                .uri(InvestController.INVEST_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json(response);
        final List<UserInvestEntity> investEntities = userInvestEntityRepository.findAll();
        Assertions.assertEquals(1, investEntities.size());
        var userInvestEntity = investEntities.get(0);
        Assertions.assertEquals(new UserInvestEntity(1L, "1", 504.5), userInvestEntity);
    }

    @TestConfiguration
    static class TestClockConfiguration {

        @Bean
        @Primary
        public Clock testClock() {
            return Clock.fixed(Instant.parse("2023-02-05T00:00:00.00Z"), Clock.systemUTC().getZone());
        }
    }
}
