package com.acme.jga.domain.functions.users.impl;

import com.acme.jga.crypto.encode.CryptoEncoder;
import com.acme.jga.domain.annotations.DomainService;
import com.acme.jga.domain.events.EventPublisher;
import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.exceptions.Scope;
import com.acme.jga.domain.functions.events.builders.users.EventUserHolder;
import com.acme.jga.domain.functions.users.validation.UserCreateValidationHolder;
import com.acme.jga.domain.i18n.BundleFactory;
import com.acme.jga.domain.input.functions.organizations.OrganizationFindInput;
import com.acme.jga.domain.input.functions.tenants.TenantFindInput;
import com.acme.jga.domain.input.functions.users.UserCreateInput;
import com.acme.jga.domain.input.functions.users.UserFindInput;
import com.acme.jga.domain.model.event.AuditAction;
import com.acme.jga.domain.model.event.AuditChange;
import com.acme.jga.domain.model.event.EventData;
import com.acme.jga.domain.model.generic.CompositeId;
import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.model.tenant.Tenant;
import com.acme.jga.domain.model.user.User;
import com.acme.jga.domain.output.functions.users.UserCreateOutput;
import com.acme.jga.domain.output.functions.users.UserFindOutput;
import com.acme.jga.domain.security.holders.ContextUserHolder;

import java.util.List;

@DomainService
public class UserCreateFuncImpl extends UserEventFunc implements UserCreateInput {
    private final OrganizationFindInput organizationFindInput;
    private final TenantFindInput tenantFindInput;
    private final UserCreateOutput userCreateOutput;
    private final CryptoEncoder cryptoEncoder;
    private final EventPublisher eventPublisher;
    private final ContextUserHolder contextUserHolder;
    private final UserFindOutput userFindOutput;

    public UserCreateFuncImpl(OrganizationFindInput organizationFindInput,
                              TenantFindInput tenantFindInput,
                              UserCreateOutput userCreateOutput,
                              CryptoEncoder cryptoEncoder,
                              EventPublisher eventPublisher,
                              UserFindInput userFindInput,
                              ContextUserHolder contextUserHolder, UserFindOutput userFindOutput) {
        this.organizationFindInput = organizationFindInput;
        this.tenantFindInput = tenantFindInput;
        this.userCreateOutput = userCreateOutput;
        this.cryptoEncoder = cryptoEncoder;
        this.eventPublisher = eventPublisher;
        this.userFindOutput = userFindOutput;
        this.contextUserHolder = contextUserHolder;
    }

    @Override
    public CompositeId create(User user) throws FunctionalException {
        // Validate payload
        UserCreateValidationHolder.getInsance().validate(user);
        Tenant tenant = tenantFindInput.findById(user.tenantId());
        Organization organization = organizationFindInput.findById(tenant.id(), user.organizationId());

        if (userFindOutput.emailUsed(user.email())) {
            throw new FunctionalException(Scope.USER.name(), FunctionalErrors.ALREADY_EXISTS.name(), BundleFactory.getMessage("user_email_used", user.email()));
        }

        if (userFindOutput.loginUsed(user.login())) {
            throw new FunctionalException(Scope.USER.name(), FunctionalErrors.ALREADY_EXISTS.name(), BundleFactory.getMessage("user_login_used", user.login()));
        }

        User usr = new User(null, tenant.id(), organization.id(),
                user.login(), user.firstName(), user.lastName(), user.middleName(), user.email(),
                user.status(), user.notifEmail(), cryptoEncoder.encode(user.secrets()));
        List<AuditChange> auditChanges = EventUserHolder.getInstance().build(null, usr);
        EventData eventData = super.buildEventData(contextUserHolder.getCurrentUser(), AuditAction.CREATE, null, tenant, organization, auditChanges);
        CompositeId userId = userCreateOutput.save(usr, eventData);
        this.eventPublisher.pushAuditEvents();
        return userId;
    }

}
