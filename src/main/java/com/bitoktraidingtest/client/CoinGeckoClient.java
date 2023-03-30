package com.bitoktraidingtest.client;

import com.bitoktraidingtest.client.dto.BtcCurrentPrice;
import com.bitoktraidingtest.client.dto.HistoricalBtcPrice;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@FeignClient(name = "coin-gecko-client", url = "${client.rest.feign.coingecko.url}")
public interface CoinGeckoClient {

    @GetMapping("/simple/price?vs_currencies=usd&ids=bitcoin")
    BtcCurrentPrice getCurrentBtcPriceInUsd();

    @GetMapping("/coins/bitcoin/history")
    HistoricalBtcPrice getHistoricalBtcPriceInUsd(
            @RequestParam(name = "date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "dd-MM-yyyy")
            LocalDate historicalDate
    );
}
