package com.bitoktraidingtest.service;

import com.bitoktraidingtest.client.CoinGeckoClient;
import com.bitoktraidingtest.client.dto.BtcCurrentPrice;
import com.bitoktraidingtest.client.dto.HistoricalBtcPrice;
import com.bitoktraidingtest.domain.InvestRequest;
import com.bitoktraidingtest.entity.UserInvestEntity;
import com.bitoktraidingtest.repository.UserInvestEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class DefaultInvestSuggestionService implements InvestSuggestionService {

    private final CoinGeckoClient coinGeckoClient;
    private final Clock clock;
    private final UserInvestEntityRepository userInvestEntityRepository;

    @Override
    public boolean shouldInvest() {
        final var now = LocalDate.now(clock);
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

    @Override
    @Transactional
    public UserInvestEntity invest(InvestRequest investRequest) {
        final var userInvestEntity = new UserInvestEntity();
        userInvestEntity.setUserId(investRequest.getUserId());
        userInvestEntity.setAmount(investRequest.getAmount());
        return userInvestEntityRepository.save(userInvestEntity);
    }
}
