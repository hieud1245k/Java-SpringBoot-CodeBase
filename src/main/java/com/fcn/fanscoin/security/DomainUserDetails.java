package com.fcn.fanscoin.security;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
@Builder
public class DomainUserDetails
        implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private Long userId;
    private String userNo;
    private String nickname;
    private Collection<? extends GrantedAuthority> authorities;

    public DomainUserDetails(final Long id,
                             final String email,
                             final String password,
                             final Long userId,
                             final String userNo,
                             final String nickname,
                             final Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = email;
        this.password = password;
        this.userId = userId;
        this.userNo = userNo;
        this.nickname = nickname;
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
