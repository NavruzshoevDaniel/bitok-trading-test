package com.bitoktraidingtest.component;

import com.bitoktraidingtest.BaseIntegrationTest;
import com.bitoktraidingtest.client.CoinGeckoClient;
import com.bitoktraidingtest.client.dto.BtcCurrentPrice;
import com.bitoktraidingtest.client.dto.HistoricalBtcPrice;
import com.bitoktraidingtest.client.dto.UsdPrice;
import com.bitoktraidingtest.controller.InvestController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;

import static org.mockito.Mockito.when;

class InvestControllerComponentTestV2 extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CoinGeckoClient coinGeckoClient;

    @Captor
    private ArgumentCaptor<LocalDate> localDateArgumentCaptor;

    @Test
    void shouldInvestTest() {
        //Given
        final String response = """
                {
                "shouldInvest": true,
                "message": "Фиксируем прибыль!"
                }
                """;
        when(coinGeckoClient.getCurrentBtcPriceInUsd()).thenReturn(
                BtcCurrentPrice.builder()
                        .usdPrice(new UsdPrice(10d))
                        .build()
        );
        when(coinGeckoClient.getHistoricalBtcPriceInUsd(localDateArgumentCaptor.capture())).thenReturn(
                HistoricalBtcPrice.builder()
                        .marketData(new HistoricalBtcPrice.MarketData(new UsdPrice(9d)))
                        .build()
        );
        //When & Then
        webTestClient.get()
                .uri(InvestController.SHOULD_INVEST_PATH)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json(response);
        final var argumentCaptorValue = localDateArgumentCaptor.getValue();
        Assertions.assertEquals(LocalDate.of(2023, Month.FEBRUARY, 4), argumentCaptorValue);
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
