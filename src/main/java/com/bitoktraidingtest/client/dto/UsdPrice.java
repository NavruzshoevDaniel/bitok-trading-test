package com.bitoktraidingtest.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UsdPrice {
    @JsonProperty("usd")
    private Double price;
}
