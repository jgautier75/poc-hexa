package com.acme.jga.domain.functions.stubs.tenants;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.output.functions.tenants.TenantDeleteOuput;

public class TenantDeleteOutputStub implements TenantDeleteOuput {
    @Override
    public boolean deleteTenant(CompositeId compositeId) {
        return true;
    }
}
