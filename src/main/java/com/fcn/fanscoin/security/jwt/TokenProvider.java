package com.fcn.fanscoin.security.jwt;

import com.fcn.fanscoin.security.DomainUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import tech.jhipster.config.JHipsterProperties;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "role";
    private static final String USER_NO = "userNo";
    private static final String USER_ID = "userId";
    private static final String NICKNAME = "nickname";

    private final Key key;
    private final JwtParser jwtParser;
    private final long tokenValidityInMilliseconds;
    private final long tokenValidityInMillisecondsForRememberMe;

    public TokenProvider(final JHipsterProperties jHipsterProperties) {
        byte[] keyBytes;
        String secret = jHipsterProperties.getSecurity().getAuthentication().getJwt().getBase64Secret();
        if (!ObjectUtils.isEmpty(secret)) {
            log.debug("Using a Base64-encoded JWT secret key");
            keyBytes = Decoders.BASE64.decode(secret);
        } else {
            log.warn("Warning: the JWT key used is not Base64-encoded. We recommend using the "
                             + "`jhipster.security.authentication.jwt.base64-secret` key for optimum security."
            );
            secret = jHipsterProperties.getSecurity().getAuthentication().getJwt().getSecret();
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }
        key = Keys.hmacShaKeyFor(keyBytes);
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        tokenValidityInMilliseconds = 1000 * jHipsterProperties.getSecurity()
                                                               .getAuthentication()
                                                               .getJwt()
                                                               .getTokenValidityInSeconds();
        tokenValidityInMillisecondsForRememberMe =
                1000 * jHipsterProperties.getSecurity()
                                         .getAuthentication()
                                         .getJwt()
                                         .getTokenValidityInSecondsForRememberMe();
    }

    public String createToken(final Authentication authentication, final boolean rememberMe) {
        String authorities = authentication.getAuthorities()
                                           .stream()
                                           .map(GrantedAuthority::getAuthority)
                                           .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + tokenValidityInMilliseconds);
        }

        DomainUserDetails domainUserDetails = (DomainUserDetails) authentication.getPrincipal();

        return Jwts.builder()
                   .setSubject(authentication.getName())
                   .claim(AUTHORITIES_KEY, authorities)
                   .claim(USER_NO, domainUserDetails.getUserNo())
                   .claim(USER_ID, domainUserDetails.getUserId())
                   .claim(NICKNAME, domainUserDetails.getNickname())
                   .signWith(key, SignatureAlgorithm.HS512)
                   .setExpiration(validity)
                   .compact();
    }

    public Authentication getAuthentication(final String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();

        Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .filter(auth -> !auth.trim().isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        DomainUserDetails domainUserDetails = DomainUserDetails.builder()
                                                               .id(null)
                                                               .username(claims.getSubject())
                                                               .password(null)
                                                               .userId(Long.parseLong(claims.get(USER_ID).toString()))
                                                               .userNo(claims.get(USER_NO).toString())
                                                               .nickname(claims.get(NICKNAME).toString())
                                                               .authorities(authorities)
                                                               .build();

        return new UsernamePasswordAuthenticationToken(domainUserDetails, token, authorities);
    }

    public boolean validateToken(final String authToken) {
        try {
            jwtParser.parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace.", e);
        }
        return false;
    }
}
