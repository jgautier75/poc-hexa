package com.acme.jga.spi.jdbc.extractors;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.sector.Sector;
import com.acme.jga.spi.jdbc.utils.DaoConstants;
import com.acme.jga.spi.jdbc.utils.SQLExtractor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SectorExtractor {
    public static Sector extractSector(ResultSet resultSet, boolean checkNext, CompositeId tenantId, CompositeId organizationId) throws SQLException {
        Sector sector = null;
        if (!checkNext || resultSet.next()) {
            CompositeId id = new CompositeId(SQLExtractor.extractLong(resultSet, DaoConstants.FIELD_ID),
                    SQLExtractor.extractString(resultSet, DaoConstants.FIELD_UID));
            Long parentSectorId = SQLExtractor.extractLong(resultSet, "parent_id");
            CompositeId parentId = new CompositeId(parentSectorId, null);
            sector = new Sector(id, tenantId, organizationId,
                    SQLExtractor.extractString(resultSet, DaoConstants.FIELD_LABEL),
                    SQLExtractor.extractString(resultSet, DaoConstants.FIELD_CODE),
                    parentId,
                    SQLExtractor.extractBoolean(resultSet, "root"),
                    null
            );
        }
        return sector;
    }
}
