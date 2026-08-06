package com.acme.jga.rest.controllers;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.adapters.rest.users.api.AppUsersService;
import com.acme.jga.rest.dtos.v1.kcspi.UserOidcDto;
import com.acme.jga.rest.utils.WebApiVersions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpiController {
    private final AppUsersService appUsersService;

    public SpiController(AppUsersService appUsersService) {
        this.appUsersService = appUsersService;
    }

    @GetMapping(value = WebApiVersions.SpiResourceVersion.FIND_USER)
    public ResponseEntity<UserOidcDto> fetchUser(@RequestParam(value = "field", required = false) String field,
                                                 @RequestParam(value = "value", required = false) String value) throws FunctionalException {
        UserOidcDto user = appUsersService.findForOidc(null, null, field, value);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
