package com.acme.jga.rest.dtos.v1.tenants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class TenantListDisplayDto {
    List<TenantDisplayDto> tenants;
}
