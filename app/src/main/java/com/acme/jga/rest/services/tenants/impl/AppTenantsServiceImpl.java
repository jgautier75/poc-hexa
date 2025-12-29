package com.acme.jga.rest.services.tenants.impl;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.input.functions.tenants.*;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.tenant.Tenant;
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
    private final TenantDeleteInput tenantDeleteInput;

    public AppTenantsServiceImpl(TenantCreateInput tenantCreateInput, TenantFindInput tenantFindInput,
                                 TenantListInput tenantListInput, TenantUpdateInput tenantUpdateInput, TenantDeleteInput tenantDeleteInput) {
        this.tenantCreateInput = tenantCreateInput;
        this.tenantFindInput = tenantFindInput;
        this.tenantListInput = tenantListInput;
        this.tenantUpdateInput = tenantUpdateInput;
        this.tenantDeleteInput = tenantDeleteInput;
    }

    @Override
    public UidDto createTenant(TenantDto tenantDto) throws FunctionalException {
        CompositeId tenantId = tenantCreateInput.create(new Tenant(null, tenantDto.getCode(), tenantDto.getLabel(), tenantDto.getStatus()));
        return new UidDto(tenantId.get());
    }

    @Override
    public TenantDisplayDto findByUid(String uid) throws FunctionalException {
        Tenant tenant = tenantFindInput.findById(new CompositeId(null, uid), null);
        return new TenantDisplayDto(tenant.id().externalId(), tenant.code(), tenant.label(), tenant.status());
    }

    @Override
    public TenantListDisplayDto findAll() throws FunctionalException {
        List<Tenant> tenants = tenantListInput.list();
        List<TenantDisplayDto> displayDtoList = StreamUtil.ofNullableList(tenants).map(t -> new TenantDisplayDto(t.id().externalId(), t.code(), t.label(), t.status())).toList();
        return new TenantListDisplayDto(displayDtoList);
    }

    @Override
    public boolean updateTenant(String uid, TenantDto tenantDto) throws FunctionalException {
        Tenant tenant = new Tenant(new CompositeId(null, uid), tenantDto.getCode(), tenantDto.getLabel(), tenantDto.getStatus());
        return tenantUpdateInput.update(tenant);
    }

    @Override
    public boolean deleteTenant(String uid) throws FunctionalException {
        return tenantDeleteInput.deleteTenant(() -> uid);
    }
}
