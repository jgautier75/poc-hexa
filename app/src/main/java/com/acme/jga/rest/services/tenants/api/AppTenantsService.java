package com.acme.jga.rest.services.tenants.api;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.rest.dtos.v1.tenants.TenantDisplayDto;
import com.acme.jga.rest.dtos.v1.tenants.TenantDto;
import com.acme.jga.rest.dtos.v1.tenants.TenantListDisplayDto;
import com.acme.jga.rest.dtos.v1.tenants.UidDto;

public interface AppTenantsService {

    UidDto createTenant(TenantDto tenantDto) throws FunctionalException;

    TenantDisplayDto findByUid(String uid) throws FunctionalException;

    TenantListDisplayDto findAll() throws FunctionalException;

    boolean updateTenant(String uid,TenantDto tenantDto) throws FunctionalException;

    boolean deleteTenant(String uid) throws FunctionalException;
}
