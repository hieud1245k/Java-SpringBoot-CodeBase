package com.fcn.fanscoin.security;

import com.fcn.fanscoin.constant.Constant;
import com.fcn.fanscoin.helper.SecurityHelper;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SpringSecurityAuditorAware
        implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityHelper.getUserNo().orElse(Constant.SYSTEM));
    }
}
