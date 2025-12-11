package com.acme.jga.rest.dtos.v1.organizations;

import com.acme.jga.domain.model.organization.OrganizationKind;
import com.acme.jga.domain.model.organization.OrganizationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class OrganizationDto {
    private String uid;
    private String code;
    private String label;
    private OrganizationKind kind;
    private String country;
    private OrganizationStatus status;
}
