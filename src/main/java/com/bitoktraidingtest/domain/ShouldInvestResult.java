package com.bitoktraidingtest.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ShouldInvestResult {
    private boolean shouldInvest;
    private String message;
}
