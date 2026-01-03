package com.acme.jga.domain.functions.users.impl;

import com.acme.jga.crypto.encode.CryptoEncoder;
import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.input.functions.organizations.OrganizationFindInput;
import com.acme.jga.domain.input.functions.tenants.TenantFindInput;
import com.acme.jga.domain.input.functions.users.UserCreateInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.model.user.User;
import com.acme.jga.domain.output.functions.users.UserCreateOutput;

@DomainService
public class UserCreateFuncImpl implements UserCreateInput {
    private final OrganizationFindInput organizationFindInput;
    private final TenantFindInput tenantFindInput;
    private final UserCreateOutput userCreateOutput;
    private final CryptoEncoder cryptoEncoder;

    public UserCreateFuncImpl(OrganizationFindInput organizationFindInput, TenantFindInput tenantFindInput, UserCreateOutput userCreateOutput, CryptoEncoder cryptoEncoder) {
        this.organizationFindInput = organizationFindInput;
        this.tenantFindInput = tenantFindInput;
        this.userCreateOutput = userCreateOutput;
        this.cryptoEncoder = cryptoEncoder;
    }

    @Override
    public CompositeId create(User user) throws FunctionalException {
        Tenant tenant = tenantFindInput.findById(user.tenantId());
        Organization organization = organizationFindInput.findById(tenant.id(), user.organizationId());
        User usr = new User(null, tenant.id(), organization.id(),
                user.login(), user.firstName(), user.lastName(), user.middleName(), user.email(),
                user.status(), user.notifEmail(), cryptoEncoder.encode(user.secrets()));
        return userCreateOutput.save(usr);
    }
}
