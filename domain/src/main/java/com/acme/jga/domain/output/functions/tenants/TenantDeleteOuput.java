package com.acme.jga.domain.output.functions.tenants;

import com.acme.jga.domain.model.generic.CompositeId;

public interface TenantDeleteOuput {
    boolean deleteTenant(CompositeId compositeId);
}
