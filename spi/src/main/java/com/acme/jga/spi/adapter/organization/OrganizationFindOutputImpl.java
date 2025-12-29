package com.acme.jga.spi.adapter.organization;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.output.functions.organizations.OrganizationFindOutput;
import com.acme.jga.spi.dao.organizations.api.OrganizationsDao;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationFindOutputImpl implements OrganizationFindOutput {
    private final OrganizationsDao organizationsDao;
    private final ObservationRegistry observationRegistry;

    public OrganizationFindOutputImpl(OrganizationsDao organizationsDao, ObservationRegistry observationRegistry) {
        this.organizationsDao = organizationsDao;
        this.observationRegistry = observationRegistry;
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
    public List<Organization> findAll(CompositeId tenantId, Observation parentObservation) {
        Observation spiObservation = Observation.createNotStarted("ORGS_SPI_LIST", observationRegistry);
        spiObservation.parentObservation(parentObservation);
        spiObservation.start();
        try {
            return this.organizationsDao.findAll(tenantId);
        } finally {
            spiObservation.stop();
        }
    }

    @Override
    public Organization findByCode(String code) {
        return this.organizationsDao.findByCode(code);
    }
}
