package com.acme.jga.rest.dtos.v1.tenants;

import com.acme.jga.domain.model.tenant.TenantStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class TenantDisplayDto {
    private String uid;
    private String code;
    private String label;
    private TenantStatus status;
}
