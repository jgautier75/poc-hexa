package com.acme.jga.rest.dtos.v1.organizations;

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
    List<OrganizationDto> organizations;
    Integer totalCount;
    Integer pageIndex;
    Integer totalNbPages;
}
