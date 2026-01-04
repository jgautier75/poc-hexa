package com.acme.jga.rest.services.users.impl;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.input.functions.users.UserCreateInput;
import com.acme.jga.domain.input.functions.users.UserFindInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.generic.PaginatedResults;
import com.acme.jga.domain.model.user.User;
import com.acme.jga.rest.dtos.shared.Pagination;
import com.acme.jga.rest.dtos.v1.tenants.UidDto;
import com.acme.jga.rest.dtos.v1.users.UserDisplayDto;
import com.acme.jga.rest.dtos.v1.users.UserDisplayListDto;
import com.acme.jga.rest.dtos.v1.users.UserDto;
import com.acme.jga.rest.services.users.api.AppUsersService;
import com.acme.jga.search.filtering.constants.SearchParams;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AppUsersServiceImpl implements AppUsersService {
    private final UserCreateInput userCreateInput;
    private final UserFindInput userFindInput;

    public AppUsersServiceImpl(UserCreateInput userCreateInput, UserFindInput userFindInput) {
        this.userCreateInput = userCreateInput;
        this.userFindInput = userFindInput;
    }

    @Override
    public UidDto create(String tenantUid, String organizationUid, UserDto userDto) throws FunctionalException {
        User usr = new User(null, new CompositeId(null, tenantUid), new CompositeId(null, organizationUid),
                userDto.getLogin(), userDto.getFirstName(), userDto.getLastName(),
                userDto.getMiddleName(), userDto.getEmail(), userDto.getStatus(), userDto.getNotifEmail(), userDto.getSecrets());
        CompositeId compositeId = userCreateInput.create(usr);
        return new UidDto(compositeId.externalId());
    }

    @Override
    public UserDisplayListDto findAll(String tenantUid, String organizationUid, Map<SearchParams, Object> searchParams) throws FunctionalException {
        PaginatedResults<User> pres = userFindInput.findAll(new CompositeId(null, tenantUid), new CompositeId(null, organizationUid), searchParams);
        List<UserDisplayDto> displayDtos = pres.results().stream().map(usr -> new UserDisplayDto(usr.login(), usr.firstName(), usr.lastName(), usr.middleName(), usr.email(), usr.status(), usr.notifEmail())).toList();
        return new UserDisplayListDto(displayDtos,new Pagination(pres.nbResults(), pres.pageIndex(), pres.nbPages()));
    }
}
