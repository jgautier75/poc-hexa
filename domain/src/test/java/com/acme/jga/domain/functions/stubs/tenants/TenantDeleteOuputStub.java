package com.acme.jga.domain.functions.stubs.tenants;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.output.functions.tenants.TenantDeleteOuput;

import java.util.List;

public class TenantDeleteOuputStub implements TenantDeleteOuput {

    private final List<CompositeId> compositeIds;

    public TenantDeleteOuputStub(List<CompositeId> compositeIds) {
        this.compositeIds = compositeIds;
    }

    @Override
    public boolean deleteTenant(CompositeId compositeId) {
        return compositeIds.stream().anyMatch(cid -> compositeId.externalId() != null && cid.externalId().equals(compositeId.externalId())
                || cid.internalId() != null && cid.internalId().longValue() == compositeId.internalId().longValue());
    }
}
