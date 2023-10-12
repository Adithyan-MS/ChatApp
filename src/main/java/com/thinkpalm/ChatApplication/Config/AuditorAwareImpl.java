package com.thinkpalm.ChatApplication.Config;

import com.thinkpalm.ChatApplication.Config.SecurityConfigure;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        return Optional.of(currentUser);
    }
}
