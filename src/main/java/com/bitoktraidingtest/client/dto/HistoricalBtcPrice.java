package com.bitoktraidingtest.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricalBtcPrice {
    @JsonProperty("market_data")
    private MarketData marketData;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MarketData {
        @JsonProperty("current_price")
        private UsdPrice usdPrice;
    }
}
