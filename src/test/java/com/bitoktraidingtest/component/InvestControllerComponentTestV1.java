package com.bitoktraidingtest.component;

import com.bitoktraidingtest.controller.InvestController;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Clock;
import java.time.Instant;

import static com.github.tomakehurst.wiremock.client.WireMock.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0, files = "classpath:/integration")
@TestPropertySource(properties = "client.rest.feign.coingecko.url=http://localhost:${wiremock.server.port}")
class InvestControllerComponentTestV1 {

    @Autowired
    private WebTestClient webTestClient;

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

    @TestConfiguration
    static class TestClockConfiguration {

        @Bean
        @Primary
        public Clock testClock() {
            return Clock.fixed(Instant.parse("2023-02-05T00:00:00.00Z"), Clock.systemUTC().getZone());
        }
    }
}
