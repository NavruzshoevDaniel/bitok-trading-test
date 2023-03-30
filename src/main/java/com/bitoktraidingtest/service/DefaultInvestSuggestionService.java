package com.bitoktraidingtest.service;

import com.bitoktraidingtest.client.CoinGeckoClient;
import com.bitoktraidingtest.client.dto.BtcCurrentPrice;
import com.bitoktraidingtest.client.dto.HistoricalBtcPrice;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class DefaultInvestSuggestionService implements InvestSuggestionService {

    private final CoinGeckoClient coinGeckoClient;

    @Override
    public boolean shouldInvest() {
        final var now = LocalDate.now();
        final BtcCurrentPrice currentBtcPriceInUsdData = coinGeckoClient.getCurrentBtcPriceInUsd();
        final HistoricalBtcPrice historicalBtcPriceInUsdData = coinGeckoClient.getHistoricalBtcPriceInUsd(now.minusDays(1));

        final double currentBtcPriceInUsd = currentBtcPriceInUsdData.getUsdPrice().getPrice();
        final double historicalBtcPriceInUsd = historicalBtcPriceInUsdData.getMarketData().getUsdPrice().getPrice();

        if (currentBtcPriceInUsd > historicalBtcPriceInUsd) {
            return true;
        } else {
            return false;
        }
    }
}
