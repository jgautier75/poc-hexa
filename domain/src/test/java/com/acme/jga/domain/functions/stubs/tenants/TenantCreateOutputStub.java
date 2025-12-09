package com.acme.jga.domain.functions.stubs.tenants;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.generic.IdKind;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.output.functions.tenants.TenantCreateOutput;

import java.util.UUID;

public class TenantCreateOutputStub implements TenantCreateOutput {

    @Override
    public CompositeId save(Tenant tenant) {
        return new CompositeId(IdKind.BOTH, 1l, UUID.randomUUID().toString());
    }
}
