package com.igitras.uaa.custom.security;

import static com.igitras.common.utils.Constants.Account.SYSTEM;

import com.igitras.common.utils.SecurityUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 * @author mason
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {
    @Override
    public String getCurrentAuditor() {
        String userName = SecurityUtils.getCurrentUserLogin();
        return (userName != null ? userName : SYSTEM);
    }
}
