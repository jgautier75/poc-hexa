package com.acme.jga.spi.dao.users.api;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.metadata.EntityMetaData;
import com.acme.jga.domain.model.user.User;
import com.acme.jga.domain.model.user.UserList;
import com.acme.jga.search.filtering.constants.SearchParams;
import org.springframework.batch.infrastructure.item.database.JdbcCursorItemReader;

import java.util.List;
import java.util.Map;

public interface UsersDao {
    CompositeId create(User user);

    boolean emailExists(String email);

    boolean loginExists(String login);

    User findById(CompositeId tenantId, CompositeId organizationId, CompositeId id);

    List<User> findAll(CompositeId tenantId, CompositeId organizationId, Map<SearchParams,Object> searchParams);

    Integer countAll(CompositeId tenantId, CompositeId organizationId, Map<SearchParams,Object> searchParams);

    Integer update(User user);

    Integer deleteUser(CompositeId id, CompositeId tenantId, CompositeId organizationId);

    Integer deleteByOrganization(CompositeId tenantId, CompositeId organizationId);

    Integer deleteByTenant(CompositeId tenantId);

    User findBySingleCriteria(CompositeId tenantId, CompositeId organizationId, EntityMetaData searchKey);

    JdbcCursorItemReader<User> usersCursor();

    void updateDiacritic(Long id, String searchFirstName, String searchLastName);

    List<UserList> filter(CompositeId tenantId, CompositeId organizationId, Map<SearchParams,Object> searchParams);

}
