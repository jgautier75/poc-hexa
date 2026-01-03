package com.acme.jga.spi.jdbc.extractors;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.user.User;
import com.acme.jga.domain.model.user.UserStatus;
import com.acme.jga.spi.jdbc.utils.DaoConstants;
import com.acme.jga.spi.jdbc.utils.SQLExtractor;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor
public class UserExtractor {
    public static User extractUser(ResultSet resultSet, boolean checkNext, CompositeId tenantId, CompositeId organizationId) throws SQLException {
        User user = null;
        if (!checkNext || resultSet.next()) {
            CompositeId id = new CompositeId(SQLExtractor.extractLong(resultSet, DaoConstants.FIELD_ID),
                    SQLExtractor.extractString(resultSet, DaoConstants.FIELD_UID));
            user = new User(id, tenantId, organizationId,
                    SQLExtractor.extractString(resultSet, DaoConstants.FIELD_LOGIN),
                    SQLExtractor.extractString(resultSet, "first_name"),
                    SQLExtractor.extractString(resultSet, "last_name"),
                    SQLExtractor.extractString(resultSet, "middle_name"),
                    SQLExtractor.extractString(resultSet, DaoConstants.FIELD_EMAIL),
                    UserStatus.fromValue(SQLExtractor.extractInteger(resultSet, DaoConstants.FIELD_STATUS)),
                    SQLExtractor.extractString(resultSet, "notif_email"),
                    SQLExtractor.extractString(resultSet, "secrets"));
        }
        return user;
    }
}
