package com.acme.jga.rest.services.tenants.impl;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.input.functions.tenants.TenantCreateInput;
import com.acme.jga.domain.input.functions.tenants.TenantFindInput;
import com.acme.jga.domain.input.functions.tenants.TenantListInput;
import com.acme.jga.domain.input.functions.tenants.TenantUpdateInput;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.model.tenant.TenantId;
import com.acme.jga.domain.shared.StreamUtil;
import com.acme.jga.rest.dtos.v1.tenants.TenantDisplayDto;
import com.acme.jga.rest.dtos.v1.tenants.TenantDto;
import com.acme.jga.rest.dtos.v1.tenants.TenantListDisplayDto;
import com.acme.jga.rest.dtos.v1.tenants.UidDto;
import com.acme.jga.rest.services.tenants.api.AppTenantsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppTenantsServiceImpl implements AppTenantsService {
    private final TenantCreateInput tenantCreateInput;
    private final TenantFindInput tenantFindInput;
    private final TenantListInput tenantListInput;
    private final TenantUpdateInput tenantUpdateInput;

    public AppTenantsServiceImpl(TenantCreateInput tenantCreateInput, TenantFindInput tenantFindInput,
                                 TenantListInput tenantListInput, TenantUpdateInput tenantUpdateInput) {
        this.tenantCreateInput = tenantCreateInput;
        this.tenantFindInput = tenantFindInput;
        this.tenantListInput = tenantListInput;
        this.tenantUpdateInput = tenantUpdateInput;
    }

    @Override
    public UidDto createTenant(TenantDto tenantDto) throws FunctionalException {
        TenantId tenantId = tenantCreateInput.create(new Tenant(null, tenantDto.getCode(), tenantDto.getLabel(), tenantDto.getStatus()));
        return new UidDto(tenantId.get());
    }

    @Override
    public TenantDisplayDto findByUid(String uid) throws FunctionalException {
        Tenant tenant = tenantFindInput.findById(() -> uid);
        return new TenantDisplayDto(tenant.tenantId().get(), tenant.code(), tenant.label(), tenant.tenantStatus());
    }

    @Override
    public TenantListDisplayDto findAll() throws FunctionalException {
        List<Tenant> tenants = tenantListInput.list();
        List<TenantDisplayDto> displayDtoList = StreamUtil.ofNullableList(tenants).map(t -> new TenantDisplayDto(t.tenantId().get(), t.code(), t.label(), t.tenantStatus())).toList();
        return new TenantListDisplayDto(displayDtoList);
    }

    @Override
    public boolean updateTenant(String uid, TenantDto tenantDto) throws FunctionalException {
        Tenant tenant = new Tenant(() -> uid, tenantDto.getCode(), tenantDto.getLabel(), tenantDto.getStatus());
        return tenantUpdateInput.update(tenant);
    }

    @Override
    public boolean deleteTenant(String uid) throws FunctionalException {
        return false;
    }
}
