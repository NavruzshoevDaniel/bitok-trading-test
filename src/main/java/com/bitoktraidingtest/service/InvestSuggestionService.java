package com.bitoktraidingtest.service;

import com.bitoktraidingtest.domain.InvestRequest;
import com.bitoktraidingtest.entity.UserInvestEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InvestSuggestionService {

    boolean shouldInvest();

    UserInvestEntity invest(InvestRequest investRequest);

    Page<UserInvestEntity> findAllUsersInvests(String userId, Pageable pageable);
}
