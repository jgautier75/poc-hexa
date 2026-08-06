package com.acme.jga.domain.ports.secondary.tenants;

import com.acme.jga.domain.model.tenant.Tenant;

import java.util.List;

public interface TenantListOutput {

    List<Tenant> list();

}
