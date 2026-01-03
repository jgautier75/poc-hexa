package com.acme.jga.spi.adapter.organization;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.output.functions.organizations.OrganizationFindOutput;
import com.acme.jga.search.filtering.constants.SearchParams;
import com.acme.jga.spi.dao.organizations.api.OrganizationsDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrganizationFindOutputImpl implements OrganizationFindOutput {
    private final OrganizationsDao organizationsDao;

    public OrganizationFindOutputImpl(OrganizationsDao organizationsDao) {
        this.organizationsDao = organizationsDao;
    }

    @Override
    public Organization findById(CompositeId tenantId, CompositeId organizationId) {
        return this.organizationsDao.findByTenantAndId(tenantId, organizationId);
    }

    @Override
    public boolean existsByCode(String code) {
        return this.organizationsDao.existsByCode(code);
    }

    @Override
    public List<Organization> findAll(CompositeId tenantId, Map<SearchParams, Object> searchParams) throws FunctionalException {
        return this.organizationsDao.findAll(tenantId, searchParams);
    }

    @Override
    public Integer countAll(CompositeId tenantId, Map<SearchParams, Object> searchParams) {
        return this.organizationsDao.countAll(tenantId, searchParams);
    }

    @Override
    public Organization findByCode(String code) {
        return this.organizationsDao.findByCode(code);
    }
}
