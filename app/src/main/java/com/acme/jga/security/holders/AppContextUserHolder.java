package com.acme.jga.security.holders;

import com.acme.jga.domain.security.holders.ContextUserHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AppContextUserHolder implements ContextUserHolder {

    @Override
    public String getCurrentUser() {
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
            return ((org.springframework.security.oauth2.jwt.Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClaimAsString("preferred_username");
        }
        return "unknown";
    }
}
