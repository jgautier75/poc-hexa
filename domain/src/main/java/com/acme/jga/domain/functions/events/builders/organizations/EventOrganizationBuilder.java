package com.acme.jga.domain.functions.events.builders.organizations;

import com.acme.jga.domain.functions.events.builders.AbstractEventBuilder;
import com.acme.jga.domain.model.event.AuditChange;
import com.acme.jga.domain.model.event.AuditOperation;
import com.acme.jga.domain.model.organization.Organization;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EventOrganizationBuilder extends AbstractEventBuilder {

    public List<AuditChange> build(Organization oldOrg, Organization newOrg) {
        if (oldOrg == null && newOrg != null) {
            // CREATION
            return buildOrgChangesCreation(newOrg);
        } else if (oldOrg != null && newOrg == null) {
            // DELETION
            return buildOrgChangesDeletion(oldOrg);
        } else {
            // MODIFICATION
            return buildOrgChanges(oldOrg, newOrg);
        }
    }

    private List<AuditChange> buildOrgChanges(Organization oldOrg, Organization newOrg) {
        List<AuditChange> changes = new ArrayList<>();
        Optional.ofNullable(buildChange(oldOrg.code(), newOrg.code(), "code")).map(changes::add);
        Optional.ofNullable(buildChange(oldOrg.country(), newOrg.country(), "country")).map(changes::add);
        Optional.ofNullable(buildChange(oldOrg.label(), newOrg.label(), "label")).map(changes::add);

        if (!oldOrg.kind().equals(newOrg.kind())) {
            changes.add(AuditChange.builder()
                    .from(oldOrg.kind().name())
                    .to(newOrg.kind().name())
                    .object("kind")
                    .operation(AuditOperation.UPDATE)
                    .build());
        }

        if (!oldOrg.status().equals(newOrg.status())) {
            changes.add(AuditChange.builder()
                    .from(oldOrg.status().name())
                    .to(newOrg.status().name())
                    .object("status")
                    .operation(AuditOperation.UPDATE)
                    .build());
        }

        return changes;
    }

    private List<AuditChange> buildOrgChangesCreation(Organization newOrg) {
        return List.of(
                AuditChange.builder()
                        .to(newOrg.code())
                        .object("code")
                        .operation(AuditOperation.ADD)
                        .build(),
                AuditChange.builder()
                        .to(newOrg.label())
                        .object("label")
                        .operation(AuditOperation.ADD)
                        .build(),
                AuditChange.builder()
                        .to(newOrg.country())
                        .object("country")
                        .operation(AuditOperation.ADD)
                        .build(),
                AuditChange.builder()
                        .to(newOrg.kind().getValue().toString())
                        .object("kind")
                        .operation(AuditOperation.ADD)
                        .build()
        );
    }

    private List<AuditChange> buildOrgChangesDeletion(Organization newOrg) {
        return List.of(
                AuditChange.builder()
                        .from(newOrg.code())
                        .object("code")
                        .operation(AuditOperation.REMOVE)
                        .build(),
                AuditChange.builder()
                        .from(newOrg.label())
                        .object("label")
                        .operation(AuditOperation.REMOVE)
                        .build(),
                AuditChange.builder()
                        .from(newOrg.country())
                        .object("country")
                        .operation(AuditOperation.REMOVE)
                        .build(),
                AuditChange.builder()
                        .from(newOrg.kind().getValue().toString())
                        .object("kind")
                        .operation(AuditOperation.REMOVE)
                        .build()
        );
    }

}
