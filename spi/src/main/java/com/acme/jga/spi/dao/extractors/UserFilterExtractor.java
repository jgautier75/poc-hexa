package com.acme.jga.spi.dao.extractors;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.user.UserStatus;
import com.acme.jga.domain.model.user.UserList;
import com.acme.jga.spi.dao.utils.DaoConstants;
import com.acme.jga.spi.dao.utils.SQLExtractor;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor
public class UserFilterExtractor {
    public static UserList extractUser(ResultSet resultSet, boolean checkNext, CompositeId tenantId, CompositeId organizationId) throws SQLException {
        UserList user = null;
        if (!checkNext || resultSet.next()) {
            CompositeId id = new CompositeId(SQLExtractor.extractLong(resultSet, DaoConstants.FIELD_ID),
                    SQLExtractor.extractString(resultSet, DaoConstants.FIELD_UID));
            user = new UserList(id, tenantId, organizationId,
                    SQLExtractor.extractString(resultSet, DaoConstants.FIELD_LOGIN),
                    SQLExtractor.extractString(resultSet, "first_name"),
                    SQLExtractor.extractString(resultSet, "last_name"),
                    SQLExtractor.extractString(resultSet, DaoConstants.FIELD_EMAIL),
                    UserStatus.fromValue(SQLExtractor.extractInteger(resultSet, DaoConstants.FIELD_STATUS)));
        }
        return user;
    }
}
