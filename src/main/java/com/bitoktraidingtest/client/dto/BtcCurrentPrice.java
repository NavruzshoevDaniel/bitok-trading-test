package com.bitoktraidingtest.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BtcCurrentPrice {
    @JsonProperty("bitcoin")
    private UsdPrice usdPrice;
}
