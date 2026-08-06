package com.acme.jga.config;

import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.user.User;
import com.acme.jga.domain.model.user.UserStatus;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JacksonConfigTest {

    @Test
    void testJacksonConfig() {
        JacksonConfig jacksonConfig = new JacksonConfig();
        CompositeId id = new CompositeId(1L, UUID.randomUUID().toString());
        CompositeId tenantId = new CompositeId(2L, UUID.randomUUID().toString());
        CompositeId orgId = new CompositeId(3L, UUID.randomUUID().toString());

        User user = new User(id,tenantId,orgId,"jgautier","Jerome","GAUTIER",null,"jgautier@test.fr", UserStatus.ACTIVE,null,null);
        jacksonConfig.objectMapper().writeValue(System.out, user);
    }

}