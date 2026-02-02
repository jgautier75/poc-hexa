package com.acme.jga.rest.dtos.v1.kcspi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserOidcDto {
    private String uid;
    private String login;
    private String firstName;
    private String lastName;
    private String encryptedPassword;
    private String email;
}
