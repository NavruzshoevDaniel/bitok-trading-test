package com.bitoktraidingtest.unit.service;

import com.bitoktraidingtest.client.CoinGeckoClient;
import com.bitoktraidingtest.client.dto.BtcCurrentPrice;
import com.bitoktraidingtest.client.dto.HistoricalBtcPrice;
import com.bitoktraidingtest.client.dto.UsdPrice;
import com.bitoktraidingtest.service.DefaultInvestSuggestionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultInvestSuggestionServiceTest {

    @InjectMocks
    private DefaultInvestSuggestionService defaultInvestSuggestionService;

    @Mock
    private CoinGeckoClient coinGeckoClient;

    @Spy
    private Clock clock = Clock.fixed(Instant.parse("2023-02-05T00:00:00.00Z"), Clock.systemUTC().getZone());

    @Captor
    private ArgumentCaptor<LocalDate> localDateArgumentCaptor;

    @Test
    void shouldReturnTrueWhenTodayBtcPriceMoreThanYesterdayBtcPrice() {
        //Given
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
        //When
        var shouldInvest = defaultInvestSuggestionService.shouldInvest();
        //Then
        var argumentCaptorValue = localDateArgumentCaptor.getValue();
        Assertions.assertEquals(LocalDate.of(2023, Month.FEBRUARY, 4), argumentCaptorValue);
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
        when(coinGeckoClient.getHistoricalBtcPriceInUsd(localDateArgumentCaptor.capture())).thenReturn(
                HistoricalBtcPrice.builder()
                        .marketData(new HistoricalBtcPrice.MarketData(new UsdPrice(10d)))
                        .build()
        );
        //When
        var shouldInvest = defaultInvestSuggestionService.shouldInvest();
        //Then
        var argumentCaptorValue = localDateArgumentCaptor.getValue();
        Assertions.assertEquals(LocalDate.of(2023, Month.FEBRUARY, 4), argumentCaptorValue);
        Assertions.assertFalse(shouldInvest);
    }

}