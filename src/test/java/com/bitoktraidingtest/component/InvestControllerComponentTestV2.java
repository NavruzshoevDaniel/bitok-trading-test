package com.bitoktraidingtest.component;

import com.bitoktraidingtest.BaseIntegrationTest;
import com.bitoktraidingtest.client.CoinGeckoClient;
import com.bitoktraidingtest.client.dto.BtcCurrentPrice;
import com.bitoktraidingtest.client.dto.HistoricalBtcPrice;
import com.bitoktraidingtest.client.dto.UsdPrice;
import com.bitoktraidingtest.controller.InvestController;
import com.bitoktraidingtest.entity.UserInvestEntity;
import com.bitoktraidingtest.repository.UserInvestEntityRepository;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.mockito.Mockito.when;

@Sql(value = "/integration/db/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
class InvestControllerComponentTestV2 extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CoinGeckoClient coinGeckoClient;

    @Autowired
    private UserInvestEntityRepository userInvestEntityRepository;

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
