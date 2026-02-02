package com.acme.jga.rest.dtos.v1.versions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiVersionList {
    private List<ApiVersionDto> versions;

    public void addApiVersion(ApiVersionDto apiVersionDto) {
        if (versions == null) {
            versions = new ArrayList<>();
        }
        versions.add(apiVersionDto);
    }
}
