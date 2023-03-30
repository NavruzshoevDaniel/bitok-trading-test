package com.bitoktraidingtest.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HistoricalBtcPrice {
    @JsonProperty("market_data")
    private MarketData marketData;

    @Data
    public static class MarketData {
        @JsonProperty("current_price")
        private UsdPrice usdPrice;
    }
}
