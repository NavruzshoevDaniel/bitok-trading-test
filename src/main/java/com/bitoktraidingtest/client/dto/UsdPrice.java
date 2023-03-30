package com.bitoktraidingtest.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsdPrice {
    @JsonProperty("usd")
    private Double price;
}
