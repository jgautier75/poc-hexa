package com.acme.jga.spi.adapter.system;

import com.acme.jga.domain.exceptions.TechnicalException;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.ports.output.system.SystemOrganizationsMigrate;
import com.acme.jga.spi.dao.organizations.api.OrganizationsDao;
import com.acme.jga.spi.dao.utils.SQLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.item.database.JdbcCursorItemReader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemOrganizationsMigrateImpl implements SystemOrganizationsMigrate {
    private final OrganizationsDao organizationsDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemOrganizationsMigrateImpl.class);

    public SystemOrganizationsMigrateImpl(OrganizationsDao organizationsDao) {
        this.organizationsDao = organizationsDao;
    }

    @Override
    @Transactional
    public void migrateDiacritic() {
        org.springframework.batch.infrastructure.item.ExecutionContext executionContext = new ExecutionContext();
        JdbcCursorItemReader<Organization> orgsJdbcCursorItemReader = organizationsDao.orgsCursor();
        int processed = 1;
        try {
            orgsJdbcCursorItemReader.open(executionContext);
            while (true) {
                Organization org = orgsJdbcCursorItemReader.read();
                if (org == null) {
                    break;
                } else {
                    LOGGER.info("Processing diacritic organization count {}", processed);
                    organizationsDao.updateDiacritic(org.id().internalId(), SQLUtils.diacritic(org.label()));
                    orgsJdbcCursorItemReader.setCurrentItemCount(processed);
                }
                processed++;
            }
        } catch (Exception e) {
            throw new TechnicalException("Unable to bulk process diacritic", e);
        } finally {
            orgsJdbcCursorItemReader.close();
        }
    }
}
