package com.acme.jga.rest.controllers;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.rest.dtos.v1.tenants.UidDto;
import com.acme.jga.rest.dtos.v1.users.UserDisplayListDto;
import com.acme.jga.rest.dtos.v1.users.UserDto;
import com.acme.jga.rest.dtos.v1.users.UserUpdateDto;
import com.acme.jga.rest.adapters.users.api.AppUsersService;
import com.acme.jga.rest.utils.WebApiVersions;
import com.acme.jga.search.filtering.constants.SearchParams;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
                                                        @PathVariable(value = "orgUid") String orgUid,
                                                        @RequestParam(value = "filter", required = false) String searchFilter,
                                                        @RequestParam(value = "index", required = false, defaultValue = "1") Integer pageIndex,
                                                        @RequestParam(value = "size", required = false, defaultValue = "10") Integer pageSize,
                                                        @RequestParam(value = "orderBy", required = false, defaultValue = "label") String orderBy) throws FunctionalException {
        Map<SearchParams, Object> searchParams = new HashMap<>();
        searchParams.put(SearchParams.FILTER, searchFilter);
        searchParams.put(SearchParams.PAGE_INDEX, pageIndex);
        searchParams.put(SearchParams.PAGE_SIZE, pageSize);
        searchParams.put(SearchParams.ORDER_BY, orderBy);
        UserDisplayListDto displayListDto = appUsersService.findAll(tenantUid, orgUid, searchParams);
        return new ResponseEntity<>(displayListDto, HttpStatus.OK);
    }

    @PostMapping(value = WebApiVersions.UsersResourceVersion.WITH_UID)
    public ResponseEntity<Void> updateUser(@PathVariable("tenantUid") String tenantUid,
                                           @PathVariable("orgUid") String orgUid, @PathVariable("userUid") String userUid,
                                           @RequestBody UserUpdateDto userDto) throws FunctionalException {
        appUsersService.update(tenantUid, orgUid, userDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(WebApiVersions.UsersResourceVersion.WITH_UID)
    public ResponseEntity<Void> deleteUser(@PathVariable("tenantUid") String tenantUid,
                                           @PathVariable("orgUid") String orgUid,
                                           @PathVariable("userUid") String userUid) throws FunctionalException {
        appUsersService.delete(tenantUid, orgUid, userUid);
        return ResponseEntity.noContent().build();
    }

}
