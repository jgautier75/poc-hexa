package com.acme.jga.rest.dtos.shared;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiError {
    private String scope;
    private String code;
    private String message;
    private int statusCode;
}
