package com.acme.jga.spi.jdbc.extractors;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.generic.IdKind;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.organization.OrganizationKind;
import com.acme.jga.domain.model.organization.OrganizationStatus;
import com.acme.jga.spi.jdbc.utils.DaoConstants;
import com.acme.jga.spi.jdbc.utils.SQLExtractor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationExtractor {
    public static Organization extractOrganization(ResultSet resultSet, boolean checkNext, CompositeId tenantId) throws SQLException {
        Organization org = null;
        if (!checkNext || resultSet.next()) {
            CompositeId id = new CompositeId(SQLExtractor.extractLong(resultSet, DaoConstants.FIELD_ID),
                    SQLExtractor.extractString(resultSet, DaoConstants.FIELD_UID));
            org = new Organization(id, tenantId,
                    SQLExtractor.extractString(resultSet, DaoConstants.FIELD_LABEL),
                    SQLExtractor.extractString(resultSet, DaoConstants.FIELD_CODE),
                    OrganizationKind.fromValue(SQLExtractor.extractInteger(resultSet, "kind")),
                    SQLExtractor.extractString(resultSet, "country"),
                    OrganizationStatus.fromValue(SQLExtractor.extractInteger(resultSet, DaoConstants.FIELD_STATUS))
            );
        }
        return org;
    }
}
