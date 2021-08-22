package com.fcn.fanscoin.security;

import com.fcn.fanscoin.domain.Account;
import com.fcn.fanscoin.domain.AccountRole;
import com.fcn.fanscoin.domain.User;
import com.fcn.fanscoin.exception.ObjectNotFoundException;
import com.fcn.fanscoin.exception.UserNotActivatedException;
import com.fcn.fanscoin.repository.AccountRepository;
import com.fcn.fanscoin.repository.AccountRoleRepository;
import com.fcn.fanscoin.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component("userDetailsService")
public class DomainUserDetailsService
        implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;
    private final UserRepository userRepository;

    public DomainUserDetailsService(final AccountRepository accountRepository,
                                    final AccountRoleRepository accountRoleRepository,
                                    final UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.accountRoleRepository = accountRoleRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating #{}", login);

        if (new EmailValidator().isValid(login, null)) {
            return accountRepository.findByEmailAndActivatedAndDeletedIsFalse(login, true)
                                    .map(account -> createSpringSecurityUser(login, account))
                                    .orElseThrow(() -> new UsernameNotFoundException("Account with email " + login
                                                                                             + " was not found"));
        }

        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        return accountRepository.findByEmailAndActivatedAndDeletedIsFalse(lowercaseLogin, true)
                                .map(user -> createSpringSecurityUser(lowercaseLogin, user))
                                .orElseThrow(() -> new UsernameNotFoundException("Account " + lowercaseLogin
                                                                                         + " was not found"));
    }

    private UserDetails createSpringSecurityUser(final String lowercaseLogin, final Account account) {
        Set<AccountRole> accountRoles = accountRoleRepository.fetchByAccountId(account.getId());
        User user = userRepository.findById(account.getUserId())
                                  .orElseThrow(() -> new ObjectNotFoundException("user"));

        if (!account.isActivated()) {
            throw new UserNotActivatedException("Account " + lowercaseLogin + " was not activated");
        }
        List<GrantedAuthority> grantedAuthorities = accountRoles.stream()
                                                                .map(ar -> new SimpleGrantedAuthority(ar.getName()
                                                                                                        .name()))
                                                                .collect(Collectors.toList());
        return new DomainUserDetails(account.getId(),
                                     account.getEmail(),
                                     account.getPassword(),
                                     account.getUserId(),
                                     user.getUserNo(),
                                     user.getNickname(),
                                     grantedAuthorities);
    }
}
