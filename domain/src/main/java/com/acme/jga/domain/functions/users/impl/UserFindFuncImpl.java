package com.acme.jga.domain.functions.users.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.input.functions.organizations.OrganizationFindInput;
import com.acme.jga.domain.input.functions.tenants.TenantFindInput;
import com.acme.jga.domain.input.functions.users.UserFindInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.generic.PaginatedResults;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.model.user.User;
import com.acme.jga.domain.output.functions.users.UserFindOutput;
import com.acme.jga.domain.search.SearchUtilities;
import com.acme.jga.search.filtering.constants.SearchParams;

import java.util.List;
import java.util.Map;

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
    public PaginatedResults<User> findAll(CompositeId tenantId, CompositeId organizationId, Map<SearchParams, Object> searchParams) throws FunctionalException {
        Tenant tenant = tenantFindInput.findById(tenantId);
        Organization organization = organizationFindInput.findById(tenant.id(), organizationId);
        Map<SearchParams, Object> params = SearchUtilities.checkParameters(searchParams);
        Integer nbUsers = this.userFindOutput.countAll(tenantId, organizationId, params);
        List<User> users = this.userFindOutput.findAll(tenant.id(), organization.id(), params);
        return new PaginatedResults<>(nbUsers,
                nbUsers != null ? (nbUsers / (Integer) searchParams.get(SearchParams.PAGE_SIZE) + 1) : 0,
                users,
                (Integer) searchParams.get(SearchParams.PAGE_INDEX),
                (Integer) searchParams.get(SearchParams.PAGE_SIZE)
        );
    }
}
