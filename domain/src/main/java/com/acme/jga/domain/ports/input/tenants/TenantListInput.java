package com.acme.jga.domain.ports.input.tenants;

import com.acme.jga.domain.model.tenant.Tenant;

import java.util.List;

public interface TenantListInput {

    List<Tenant> list();

}
