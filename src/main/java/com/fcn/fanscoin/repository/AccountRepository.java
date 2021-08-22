package com.fcn.fanscoin.repository;

import com.fcn.fanscoin.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

public interface AccountRepository
        extends JpaRepository<Account, Long> {

    Optional<Account> findByEmailAndActivatedAndDeletedIsFalse(String email, boolean activated);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Account a"
        + " SET a.updatedAt = :now"
        + " WHERE a.id = :id")
    void updateLastLoginAtById(@Param("id") Long id,
                               @Param("now") Instant now);
}
