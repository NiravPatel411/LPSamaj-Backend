package com.xmplify.starter_kit_springboot_singledb.config;

import com.xmplify.starter_kit_springboot_singledb.model.Admin;
import com.xmplify.starter_kit_springboot_singledb.security.UserPrincipal;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Admin> {

    @Override
    public Optional<Admin> getCurrentAuditor() {
        return Optional.ofNullable(((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAdmin());
    }
}
