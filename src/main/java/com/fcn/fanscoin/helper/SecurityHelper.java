package com.fcn.fanscoin.helper;

import com.fcn.fanscoin.enums.RoleType;
import com.fcn.fanscoin.security.DomainUserDetails;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public final class SecurityHelper {

    private SecurityHelper() {
    }

    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();

        return Optional
                .ofNullable(securityContext.getAuthentication())
                .map(
                        authentication -> {
                            if (authentication.getPrincipal() instanceof UserDetails) {
                                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                                return springSecurityUser.getUsername();
                            } else if (authentication.getPrincipal() instanceof String) {
                                return (String) authentication.getPrincipal();
                            }
                            return null;
                        }
                );
    }

    public static boolean hasRole(final RoleType roleType) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (ObjectUtils.isNotEmpty(authentication)) {
            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                if (grantedAuthority.getAuthority().equalsIgnoreCase(roleType.name())) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean hasAnyRole(final RoleType... roleTypes) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (ObjectUtils.isNotEmpty(authentication)) {
            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                for (RoleType roleType : roleTypes) {
                    if (grantedAuthority.getAuthority().equalsIgnoreCase(roleType.name())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private static String extractPrincipal(final Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            return (String) authentication.getPrincipal();
        }

        return null;
    }

    public static Long getUserId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            DomainUserDetails domainUserDetails = (DomainUserDetails) authentication.getPrincipal();
            return domainUserDetails.getUserId();
        }

        return null;
    }

    public static Optional<String> getUserNo() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            DomainUserDetails domainUserDetails = (DomainUserDetails) authentication.getPrincipal();
            return Optional.ofNullable(domainUserDetails.getUserNo());
        }

        return Optional.empty();
    }

    public static String getNickname() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            DomainUserDetails domainUserDetails = (DomainUserDetails) authentication.getPrincipal();
            return domainUserDetails.getNickname();
        }

        return null;
    }

    public static boolean isAuthenticated() {
        return getUserId() != null;
    }

    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();

        return Optional.ofNullable(securityContext.getAuthentication())
                       .filter(authentication -> authentication.getCredentials() instanceof String)
                       .map(authentication -> (String) authentication.getCredentials());
    }

    public static String getErrorViewTemplate(final String error) {
        return
                hasRole(RoleType.ADMIN)
                        ? "admin/error/".concat(error)
                        : "user/error/".concat(error);
    }
}
