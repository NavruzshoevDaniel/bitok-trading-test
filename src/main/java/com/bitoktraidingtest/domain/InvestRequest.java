package com.bitoktraidingtest.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class InvestRequest {
    @Positive
    private double amount;
    @NotBlank
    private String userId;
}
