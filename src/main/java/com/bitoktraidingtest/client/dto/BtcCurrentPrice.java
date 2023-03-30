package com.bitoktraidingtest.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BtcCurrentPrice {
    @JsonProperty("bitcoin")
    private UsdPrice usdPrice;
}
