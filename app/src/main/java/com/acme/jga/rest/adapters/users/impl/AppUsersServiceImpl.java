package com.acme.jga.rest.adapters.users.impl;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.input.functions.users.UserCreateInput;
import com.acme.jga.domain.input.functions.users.UserDeleteInput;
import com.acme.jga.domain.input.functions.users.UserFindInput;
import com.acme.jga.domain.input.functions.users.UserUpdateInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.generic.PaginatedResults;
import com.acme.jga.domain.model.user.User;
import com.acme.jga.rest.dtos.shared.Pagination;
import com.acme.jga.rest.dtos.v1.tenants.UidDto;
import com.acme.jga.rest.dtos.v1.users.UserDisplayDto;
import com.acme.jga.rest.dtos.v1.users.UserDisplayListDto;
import com.acme.jga.rest.dtos.v1.users.UserDto;
import com.acme.jga.rest.dtos.v1.users.UserUpdateDto;
import com.acme.jga.rest.adapters.users.api.AppUsersService;
import com.acme.jga.search.filtering.constants.SearchParams;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AppUsersServiceImpl implements AppUsersService {
    private final UserCreateInput userCreateInput;
    private final UserFindInput userFindInput;
    private final UserUpdateInput userUpdateInput;
    private final UserDeleteInput userDeleteInput;

    public AppUsersServiceImpl(UserCreateInput userCreateInput, UserFindInput userFindInput, UserUpdateInput userUpdateInput, UserDeleteInput userDeleteInput) {
        this.userCreateInput = userCreateInput;
        this.userFindInput = userFindInput;
        this.userUpdateInput = userUpdateInput;
        this.userDeleteInput = userDeleteInput;
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
        return new UserDisplayListDto(displayDtos, new Pagination(pres.nbResults(), pres.pageIndex(), pres.nbPages()));
    }

    @Override
    public void update(String tenantUid, String organizationUid, UserUpdateDto userUpdateDto) throws FunctionalException {
        User usr = new User(null, new CompositeId(null, tenantUid), new CompositeId(null, organizationUid),
                userUpdateDto.getLogin(), userUpdateDto.getFirstName(), userUpdateDto.getLastName(),
                userUpdateDto.getMiddleName(), userUpdateDto.getEmail(), userUpdateDto.getStatus(), userUpdateDto.getNotifEmail(), null);
        userUpdateInput.update(usr);
    }

    @Override
    public void delete(String tenantUid, String organizationUid, String uid) throws FunctionalException {
        userDeleteInput.delete(new CompositeId(null, tenantUid), new CompositeId(null, organizationUid), new CompositeId(null, uid));
    }
}
