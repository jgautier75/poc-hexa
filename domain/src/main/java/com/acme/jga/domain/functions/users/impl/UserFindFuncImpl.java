package com.acme.jga.domain.functions.users.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.input.functions.organizations.OrganizationFindInput;
import com.acme.jga.domain.input.functions.tenants.TenantFindInput;
import com.acme.jga.domain.input.functions.users.UserFindInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.model.user.User;
import com.acme.jga.domain.output.functions.users.UserFindOutput;

import java.util.List;

@DomainService
public class UserFindFuncImpl implements UserFindInput {
    private final TenantFindInput tenantFindInput;
    private final OrganizationFindInput organizationFindInput;
    private final UserFindOutput userFindOutput;

    public UserFindFuncImpl(TenantFindInput tenantFindInput, OrganizationFindInput organizationFindInput, UserFindOutput userFindOutput) {
        this.tenantFindInput = tenantFindInput;
        this.organizationFindInput = organizationFindInput;
        this.userFindOutput = userFindOutput;
    }

    @Override
    public List<User> findAll(CompositeId tenantId, CompositeId organizationId) throws FunctionalException {
        Tenant tenant = tenantFindInput.findById(tenantId);
        Organization organization = organizationFindInput.findById(tenant.id(), organizationId);
        return this.userFindOutput.findAll(tenant.id(), organization.id());
    }
}
