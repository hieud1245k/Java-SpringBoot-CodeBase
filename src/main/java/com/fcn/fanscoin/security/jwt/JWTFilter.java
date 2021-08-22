package com.fcn.fanscoin.security.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Arrays;

import static com.fcn.fanscoin.constant.Constant.ACCESS_TOKEN_COOKIE_NAME;

public class JWTFilter
        extends GenericFilterBean {

    private final TokenProvider tokenProvider;

    public JWTFilter(final TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);

        if (StringUtils.hasText(jwt) && this.tokenProvider.validateToken(jwt)) {
            Authentication authentication = this.tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String resolveToken(final HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (!ObjectUtils.isEmpty(cookies)) {
            return String.valueOf(Arrays.stream(cookies)
                                        .filter(cookie -> cookie.getName().equals(ACCESS_TOKEN_COOKIE_NAME))
                                        .findAny()
                                        .orElse(new Cookie(ACCESS_TOKEN_COOKIE_NAME, null))
                                        .getValue());
        }

        return null;
    }
}
