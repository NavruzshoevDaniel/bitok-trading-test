package com.bitoktraidingtest.controller;

import com.bitoktraidingtest.domain.ShouldInvestResult;
import com.bitoktraidingtest.service.InvestSuggestionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class InvestController {
    public static final String SHOULD_INVEST_PATH = "/should-invest";
    private final InvestSuggestionService investSuggestionService;

    @GetMapping(SHOULD_INVEST_PATH)
    public ShouldInvestResult shouldInvest() {
        if (investSuggestionService.shouldInvest()) {
            return ShouldInvestResult.builder().shouldInvest(true).message("Фиксируем прибыль!").build();
        } else {
            return ShouldInvestResult.builder().shouldInvest(false).message("Крипта - скам!").build();
        }
    }
}
