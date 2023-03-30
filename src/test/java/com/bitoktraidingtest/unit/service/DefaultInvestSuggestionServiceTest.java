package com.bitoktraidingtest.unit.service;

import com.bitoktraidingtest.client.CoinGeckoClient;
import com.bitoktraidingtest.client.dto.BtcCurrentPrice;
import com.bitoktraidingtest.client.dto.HistoricalBtcPrice;
import com.bitoktraidingtest.client.dto.UsdPrice;
import com.bitoktraidingtest.service.DefaultInvestSuggestionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultInvestSuggestionServiceTest {

    @InjectMocks
    private DefaultInvestSuggestionService defaultInvestSuggestionService;

    @Mock
    private CoinGeckoClient coinGeckoClient;

    @Test
    void shouldReturnTrueWhenTodayBtcPriceMoreThanYesterdayBtcPrice() {
        //Given
        when(coinGeckoClient.getCurrentBtcPriceInUsd()).thenReturn(
                BtcCurrentPrice.builder()
                        .usdPrice(new UsdPrice(10d))
                        .build()
        );
        when(coinGeckoClient.getHistoricalBtcPriceInUsd(any(LocalDate.class))).thenReturn(
                HistoricalBtcPrice.builder()
                        .marketData(new HistoricalBtcPrice.MarketData(new UsdPrice(9d)))
                        .build()
        );
        //When
        var shouldInvest = defaultInvestSuggestionService.shouldInvest();
        //Then
        Assertions.assertTrue(shouldInvest);
    }

    @Test
    void shouldReturnFalseWhenTodayBtcPriceMoreThanYesterdayBtcPrice() {
        //Given
        when(coinGeckoClient.getCurrentBtcPriceInUsd()).thenReturn(
                BtcCurrentPrice.builder()
                        .usdPrice(new UsdPrice(9d))
                        .build()
        );
        when(coinGeckoClient.getHistoricalBtcPriceInUsd(any(LocalDate.class))).thenReturn(
                HistoricalBtcPrice.builder()
                        .marketData(new HistoricalBtcPrice.MarketData(new UsdPrice(10d)))
                        .build()
        );
        //When
        var shouldInvest = defaultInvestSuggestionService.shouldInvest();
        //Then
        Assertions.assertFalse(shouldInvest);
    }

}