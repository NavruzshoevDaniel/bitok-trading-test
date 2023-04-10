package com.bitoktraidingtest.integration.client;

import com.bitoktraidingtest.client.CoinGeckoClient;
import com.bitoktraidingtest.client.dto.HistoricalBtcPrice;
import com.bitoktraidingtest.client.dto.UsdPrice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.Month;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(classes = FeignClientTestConfiguration.class)
/*
testImplementation("org.springframework.cloud:spring-cloud-contract-wiremock") нужна эта зависимость,
но в данном build.gradle она подтягивается из spring-cloud-starter-contract
*/
@AutoConfigureWireMock(port = 0, files = "classpath:/integration")
@TestPropertySource(properties = "client.rest.feign.coingecko.url=http://localhost:${wiremock.server.port}")
class CoinGeckoClientIntegrationTest {

    @Autowired
    private CoinGeckoClient coinGeckoClient;

    @Test
    void getHistoricalBtcPrice() {
        //Given
        stubFor(
                get(urlEqualTo("/coins/bitcoin/history?date=15-02-2023"))
                        .willReturn(
                                aResponse()
                                        .withStatus(HttpStatus.OK.value())
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBodyFile("historicalBtcPrice.json")
                        )
        );
        final var localDate = LocalDate.of(2023, Month.FEBRUARY, 15);
        //When
        final HistoricalBtcPrice historicalBtcPriceInUsd = coinGeckoClient.getHistoricalBtcPriceInUsd(localDate);
        //Then
        final HistoricalBtcPrice.MarketData marketData = historicalBtcPriceInUsd.getMarketData();
        Assertions.assertNotNull(marketData);
        final UsdPrice usdPrice = marketData.getUsdPrice();
        Assertions.assertNotNull(usdPrice);
        Assertions.assertEquals(22220.070997481067, usdPrice.getPrice());
    }
}
