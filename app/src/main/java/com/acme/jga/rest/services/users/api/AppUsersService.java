package com.acme.jga.rest.services.users.api;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.rest.dtos.v1.tenants.UidDto;
import com.acme.jga.rest.dtos.v1.users.UserDisplayListDto;
import com.acme.jga.rest.dtos.v1.users.UserDto;
import com.acme.jga.search.filtering.constants.SearchParams;

import java.util.Map;

public interface AppUsersService {
    UidDto create(String tenantUid, String organizationUid, UserDto userDto) throws FunctionalException;

    UserDisplayListDto findAll(String tenantUid, String organizationUid, Map<SearchParams, Object> searchParams) throws FunctionalException;
}
