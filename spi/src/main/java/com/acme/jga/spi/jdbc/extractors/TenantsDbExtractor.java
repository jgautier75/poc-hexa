package com.acme.jga.spi.jdbc.extractors;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.generic.IdKind;
import com.acme.jga.domain.model.tenant.TenantStatus;
import com.acme.jga.spi.jdbc.model.RdbmsTenant;
import com.acme.jga.spi.jdbc.utils.DaoConstants;
import com.acme.jga.spi.jdbc.utils.SQLExtractor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TenantsDbExtractor {
    public static RdbmsTenant extractTenant(ResultSet resultSet, boolean checkNext) throws SQLException {
        RdbmsTenant tenant = null;
        if (!checkNext || resultSet.next()) {
            CompositeId compositeId = new CompositeId(IdKind.BOTH,
                    SQLExtractor.extractLong(resultSet, DaoConstants.FIELD_ID),
                    SQLExtractor.extractString(resultSet, DaoConstants.FIELD_UID));
            tenant = new RdbmsTenant(compositeId,
                    SQLExtractor.extractString(resultSet, DaoConstants.FIELD_CODE),
                    SQLExtractor.extractString(resultSet, DaoConstants.FIELD_LABEL),
                    TenantStatus.fromValue(resultSet.getInt(DaoConstants.FIELD_STATUS)));
        }
        return tenant;
    }
}
