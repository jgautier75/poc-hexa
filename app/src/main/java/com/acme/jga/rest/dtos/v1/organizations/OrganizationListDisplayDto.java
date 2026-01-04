package com.acme.jga.rest.dtos.v1.organizations;

import com.acme.jga.rest.dtos.shared.Pagination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class OrganizationListDisplayDto {
    private List<OrganizationDto> organizations;
    private Pagination pagination;
}
