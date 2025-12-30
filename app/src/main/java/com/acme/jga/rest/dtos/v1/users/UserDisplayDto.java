package com.acme.jga.rest.dtos.v1.users;

import com.acme.jga.domain.model.user.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class UserDisplayDto {
    private String login;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private UserStatus status;
    private String notifEmail;
}
