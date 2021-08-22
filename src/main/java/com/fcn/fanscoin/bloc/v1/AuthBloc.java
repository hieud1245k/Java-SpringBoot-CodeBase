package com.fcn.fanscoin.bloc.v1;

import com.fcn.fanscoin.dto.v1.request.LoginReq;
import com.fcn.fanscoin.helper.SecurityHelper;
import com.fcn.fanscoin.security.jwt.TokenProvider;
import com.fcn.fanscoin.service.v1.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.fcn.fanscoin.constant.Constant.ACCESS_TOKEN_COOKIE_NAME;
import static tech.jhipster.config.JHipsterDefaults.Security.Authentication.Jwt.tokenValidityInSeconds;

@Slf4j
@Service
public class AuthBloc {

    private final TokenProvider tokenProvider;
    private final AccountService accountService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthBloc(final TokenProvider tokenProvider,
                    final AccountService accountService,
                    final AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.accountService = accountService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    public Cookie login(final LoginReq loginReq) {
        log.info("Login with data #{}", loginReq);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginReq.getEmail(),
                                                        loginReq.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        accountService.updateLastLoginAtById(accountService.getByEmail(loginReq.getEmail())
                                                           .getId());

        return buildCookie(authentication);
    }

    private Cookie buildCookie(final Authentication authentication) {
        String jwt = tokenProvider.createToken(authentication, false);
        Cookie cookie = new Cookie(ACCESS_TOKEN_COOKIE_NAME, jwt);
        cookie.setMaxAge(Integer.parseInt(String.valueOf(tokenValidityInSeconds)));
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    public void logout(final HttpServletRequest request, final HttpServletResponse response) {
        Long currentUserId = SecurityHelper.getUserId();
        log.info("Logout for currentUserId #{}", currentUserId);

        SecurityContextLogoutHandler contextHandler = new SecurityContextLogoutHandler();
        contextHandler.logout(request, response, null);

        Cookie cookie = new Cookie(ACCESS_TOKEN_COOKIE_NAME, "");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
    }
}
