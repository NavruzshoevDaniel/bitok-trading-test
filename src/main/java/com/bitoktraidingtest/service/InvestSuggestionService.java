package com.bitoktraidingtest.service;

import com.bitoktraidingtest.domain.InvestRequest;
import com.bitoktraidingtest.entity.UserInvestEntity;

public interface InvestSuggestionService {

    boolean shouldInvest();

    UserInvestEntity invest(InvestRequest investRequest);
}
