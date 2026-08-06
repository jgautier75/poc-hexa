package com.acme.jga.domain.ports.secondary.tenants;

import com.acme.jga.domain.model.generic.CompositeId;

public interface TenantDeleteOuput {
    boolean deleteTenant(CompositeId compositeId);
}
