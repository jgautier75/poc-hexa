package com.acme.jga.domain.functions.organizations.impl;

import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.exceptions.Scope;
import com.acme.jga.domain.i18n.BundleFactory;
import com.acme.jga.domain.input.functions.organizations.OrganizationDeleteInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.output.functions.organizations.OrganizationDeleteOutput;
import com.acme.jga.domain.output.functions.organizations.OrganizationFindOutput;

@DomainService
public class OrganizationDeleteFuncImpl implements OrganizationDeleteInput {

    private final OrganizationFindOutput organizationFindOutput;
    private final OrganizationDeleteOutput organizationDeleteOutput;

    public OrganizationDeleteFuncImpl(OrganizationFindOutput organizationFindOutput, OrganizationDeleteOutput organizationDeleteOutput) {
        this.organizationFindOutput = organizationFindOutput;
        this.organizationDeleteOutput = organizationDeleteOutput;
    }

    @Override
    public void delete(CompositeId tenantId, CompositeId orgId) throws FunctionalException {
        Organization orgDb = this.organizationFindOutput.findById(tenantId, orgId);
        if (orgDb == null) {
            throw new FunctionalException(Scope.ORGANIZATION.name(), FunctionalErrors.NOT_FOUND.name(), BundleFactory.getMessage("organization.not_found", orgId.externalId()));
        }
        this.organizationDeleteOutput.delete(tenantId, orgDb.id());
    }
}
