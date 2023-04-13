package com.bitoktraidingtest.integration.db;

import com.bitoktraidingtest.BaseIntegrationTest;
import com.bitoktraidingtest.entity.UserInvestEntity;
import com.bitoktraidingtest.repository.UserInvestEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;

import java.util.List;

@Sql(value = "/integration/db/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
class UserInvestEntityRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserInvestEntityRepository userInvestEntityRepository;

    @Test
    @Sql("/integration/db/findAllUsersInvestsByUserIdUsingPageable.sql")
    void findAllUsersInvestsByUserIdUsingPageable() {
        //When
        final Page<UserInvestEntity> allByUserId = userInvestEntityRepository.findAllByUserId(
                "1",
                Pageable.ofSize(2)
        );
        //Then
        Assertions.assertEquals(2, allByUserId.getTotalElements());
        Assertions.assertEquals(1, allByUserId.getTotalPages());
        Assertions.assertEquals(
                List.of(new UserInvestEntity(1L, "1", 500d), new UserInvestEntity(2L, "1", 200d)),
                allByUserId.getContent()
        );
    }
}
