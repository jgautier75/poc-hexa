package com.acme.jga.domain.functions.events.builders;

import com.acme.jga.domain.model.event.AuditChange;
import com.acme.jga.domain.model.event.AuditOperation;
import com.acme.jga.domain.model.organization.Organization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventOrganizationBuilder {

    public List<AuditChange> build(Organization oldOrg, Organization newOrg) {
        if (oldOrg == null && newOrg != null) {
            // CREATION
            return buildOrgChanges(newOrg, AuditOperation.ADD);
        } else if (oldOrg != null && newOrg == null) {
            // DELETION
            return buildOrgChanges(oldOrg, AuditOperation.REMOVE);
        } else {
            // MODIFICATION
        }
        return Collections.emptyList();
    }

    private List<AuditChange> buildOrgChanges(Organization oldOrg, Organization newOrg) {
        List<AuditChange> changes = new ArrayList<>();
        changes.add(buildChange(oldOrg.code(), newOrg.code(), "code"));
        changes.add(buildChange(oldOrg.country(), newOrg.country(), "country"));
        changes.add(buildChange(oldOrg.label(), newOrg.label(), "label"));

        if (oldOrg.kind().equals(newOrg.kind())) {
            changes.add(AuditChange.builder()
                    .from(oldOrg.kind().name())
                    .to(newOrg.kind().name())
                    .object("kind")
                    .operation(AuditOperation.UPDATE)
                    .build());
        }

        if (oldOrg.label().equals(newOrg.label())) {
            changes.add(AuditChange.builder()
                    .from(oldOrg.status().name())
                    .to(newOrg.status().name())
                    .object("status")
                    .operation(AuditOperation.UPDATE)
                    .build());
        }

        return changes;
    }

    private AuditChange buildChange(String from, String to, String field) {
        if (from == null && to != null) {
            return AuditChange.builder()
                    .to(to)
                    .object(field)
                    .operation(AuditOperation.ADD)
                    .build();
        } else if (from != null && to == null) {
            return AuditChange.builder()
                    .from(to)
                    .object(field)
                    .operation(AuditOperation.REMOVE)
                    .build();
        } else {
            return AuditChange.builder()
                    .from(from)
                    .to(to)
                    .object(field)
                    .operation(AuditOperation.UPDATE)
                    .build();
        }
    }

    private List<AuditChange> buildOrgChanges(Organization newOrg, AuditOperation operation) {
        return List.of(
                AuditChange.builder()
                        .to(newOrg.code())
                        .object("code")
                        .operation(operation)
                        .build(),
                AuditChange.builder()
                        .to(newOrg.label())
                        .object("label")
                        .operation(operation)
                        .build(),
                AuditChange.builder()
                        .to(newOrg.country())
                        .object("country")
                        .operation(operation)
                        .build(),
                AuditChange.builder()
                        .to(newOrg.kind().getValue().toString())
                        .object("kind")
                        .operation(operation)
                        .build(),
                AuditChange.builder()
                        .object("uuid")
                        .operation(operation)
                        .to(newOrg.id().externalId())
                        .build()
        );
    }

}
