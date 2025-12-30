package com.acme.jga.rest.controllers;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.rest.dtos.v1.tenants.UidDto;
import com.acme.jga.rest.dtos.v1.users.UserDisplayListDto;
import com.acme.jga.rest.dtos.v1.users.UserDto;
import com.acme.jga.rest.services.users.api.AppUsersService;
import com.acme.jga.rest.utils.WebApiVersions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UsersController {
    private final AppUsersService appUsersService;

    public UsersController(AppUsersService appUsersService) {
        this.appUsersService = appUsersService;
    }

    @PostMapping(value = WebApiVersions.UsersResourceVersion.ROOT)
    public ResponseEntity<UidDto> createUser(@PathVariable("tenantUid") String tenantUid,
                                             @PathVariable(value = "orgUid") String orgUid,
                                             @RequestBody UserDto userDto) throws FunctionalException {
        UidDto uidDto = appUsersService.create(tenantUid, orgUid, userDto);
        return new ResponseEntity<>(uidDto, HttpStatus.CREATED);
    }

    @GetMapping(value = WebApiVersions.UsersResourceVersion.ROOT)
    public ResponseEntity<UserDisplayListDto> listUsers(@PathVariable("tenantUid") String tenantUid,
                                                        @PathVariable(value = "orgUid") String orgUid) throws FunctionalException {
        UserDisplayListDto displayListDto = appUsersService.findAll(tenantUid, orgUid);
        return new ResponseEntity<>(displayListDto, HttpStatus.OK);
    }
}
