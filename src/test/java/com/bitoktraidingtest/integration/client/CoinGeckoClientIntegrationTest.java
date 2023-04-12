package com.bitoktraidingtest.integration.client;

import com.bitoktraidingtest.BaseIntegrationTest;
import com.bitoktraidingtest.client.CoinGeckoClient;
import com.bitoktraidingtest.client.dto.BtcCurrentPrice;
import com.bitoktraidingtest.client.dto.HistoricalBtcPrice;
import com.bitoktraidingtest.client.dto.UsdPrice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.Month;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

class CoinGeckoClientIntegrationTest extends BaseIntegrationTest {

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

    @Test
    void getCurrentBtcPrice() {
        //Given
        stubFor(
                get(urlEqualTo("/simple/price?vs_currencies=usd&ids=bitcoin"))
                        .willReturn(
                                aResponse()
                                        .withStatus(HttpStatus.OK.value())
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBodyFile("currentPrice.json")
                        )
        );
        //When
        final BtcCurrentPrice btcCurrentPrice = coinGeckoClient.getCurrentBtcPriceInUsd();
        //Then
        final UsdPrice usdPrice = btcCurrentPrice.getUsdPrice();
        Assertions.assertNotNull(usdPrice);
        Assertions.assertEquals(30121, usdPrice.getPrice());
    }
}
