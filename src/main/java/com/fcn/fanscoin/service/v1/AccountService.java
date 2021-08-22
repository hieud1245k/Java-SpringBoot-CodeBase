package com.fcn.fanscoin.service.v1;

import com.fcn.fanscoin.domain.Account;
import com.fcn.fanscoin.exception.ObjectNotFoundException;
import com.fcn.fanscoin.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(final AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional(readOnly = true)
    public Account getByEmail(final String email) {
        log.info("Get account by email #{}", email);

        return accountRepository.findByEmailAndActivatedAndDeletedIsFalse(email, true)
                                .orElseThrow(() -> new ObjectNotFoundException("account"));
    }

    @Transactional
    public void updateLastLoginAtById(final Long id) {
        log.info("Update lastLoginAt by id #{}", id);

        accountRepository.updateLastLoginAtById(id, Instant.now());
    }
}
