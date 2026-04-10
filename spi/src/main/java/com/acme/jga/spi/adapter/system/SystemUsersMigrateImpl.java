package com.acme.jga.spi.adapter.system;

import com.acme.jga.domain.exceptions.TechnicalException;
import com.acme.jga.domain.model.user.User;
import com.acme.jga.domain.ports.output.system.SystemUsersMigrate;
import com.acme.jga.spi.dao.users.api.UsersDao;
import com.acme.jga.spi.dao.utils.SQLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.item.database.JdbcCursorItemReader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemUsersMigrateImpl implements SystemUsersMigrate {
    private final UsersDao usersDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemUsersMigrateImpl.class);

    public SystemUsersMigrateImpl(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    @Override
    @Transactional
    public void migrateDiacritic() {
        org.springframework.batch.infrastructure.item.ExecutionContext executionContext = new ExecutionContext();
        JdbcCursorItemReader<User> userJdbcCursorItemReader = usersDao.usersCursor();
        int processed = 1;
        try {
            userJdbcCursorItemReader.open(executionContext);
            while (true) {
                User usr = userJdbcCursorItemReader.read();
                if (usr == null) {
                    break;
                } else {
                    LOGGER.info("Processing diacritic user count {}", processed);
                    usersDao.updateDiacritic(usr.id().internalId(), SQLUtils.diacritic(usr.firstName()), SQLUtils.diacritic(usr.lastName()));
                    userJdbcCursorItemReader.setCurrentItemCount(processed);
                }
                processed++;
            }
        } catch (Exception e) {
            throw new TechnicalException("Unable to bulk process diacritic", e);
        } finally {
            userJdbcCursorItemReader.close();
        }
    }
}
