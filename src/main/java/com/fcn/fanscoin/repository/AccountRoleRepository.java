package com.fcn.fanscoin.repository;

import com.fcn.fanscoin.domain.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface AccountRoleRepository
        extends JpaRepository<AccountRole, Long> {

    @Query("SELECT new AccountRole(ar.id, ar.accountId, ar.roleId, r.name)"
            + " FROM AccountRole ar"
            + " JOIN Role r ON r.id = ar.roleId"
            + " WHERE ar.accountId = :accountId")
    Set<AccountRole> fetchByAccountId(@Param("accountId") Long accountId);
}
