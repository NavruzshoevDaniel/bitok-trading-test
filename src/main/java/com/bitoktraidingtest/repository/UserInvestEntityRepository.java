package com.bitoktraidingtest.repository;

import com.bitoktraidingtest.entity.UserInvestEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInvestEntityRepository extends JpaRepository<UserInvestEntity, Long> {

    Page<UserInvestEntity> findAllByUserId(String userId, Pageable pageable);
}