package com.acme.jga.rest.dtos.v1.versions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiVersionDto {
    private String version;
    private String category;
    private String code;
    private String uri;
}
