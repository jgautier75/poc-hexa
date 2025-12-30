package com.acme.jga.rest.services.users.impl;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.input.functions.users.UserCreateInput;
import com.acme.jga.domain.input.functions.users.UserFindInput;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.user.User;
import com.acme.jga.rest.dtos.v1.tenants.UidDto;
import com.acme.jga.rest.dtos.v1.users.UserDisplayDto;
import com.acme.jga.rest.dtos.v1.users.UserDisplayListDto;
import com.acme.jga.rest.dtos.v1.users.UserDto;
import com.acme.jga.rest.services.users.api.AppUsersService;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public UserDisplayListDto findAll(String tenantUid, String organizationUid) throws FunctionalException {
        List<User> users = userFindInput.findAll(new CompositeId(null, tenantUid), new CompositeId(null, organizationUid));
        UserDisplayListDto listDto = new UserDisplayListDto();
        List<UserDisplayDto> displayDtos = users.stream().map(usr -> {
            return new UserDisplayDto(usr.login(), usr.firstName(), usr.lastName(), usr.middleName(), usr.email(), usr.status(), usr.notifEmail());
        }).toList();
        listDto.setUsers(displayDtos);
        return listDto;
    }
}
