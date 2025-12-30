package com.acme.jga.rest.services.users.api;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.rest.dtos.v1.tenants.UidDto;
import com.acme.jga.rest.dtos.v1.users.UserDisplayListDto;
import com.acme.jga.rest.dtos.v1.users.UserDto;

public interface AppUsersService {
    UidDto create(String tenantUid, String organizationUid, UserDto userDto) throws FunctionalException;

    UserDisplayListDto findAll(String tenantUid, String organizationUid) throws FunctionalException;
}
