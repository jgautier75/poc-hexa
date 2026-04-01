package com.acme.jga.domain.functions.events.builders.users;

import com.acme.jga.domain.functions.events.builders.AbstractEventBuilder;
import com.acme.jga.domain.model.event.AuditChange;
import com.acme.jga.domain.model.event.AuditOperation;
import com.acme.jga.domain.model.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EventUserBuilder extends AbstractEventBuilder {

    public List<AuditChange> build(User oldUser, User newUser) {
        if (oldUser == null && newUser != null) {
            // CREATION
            return buildUserChangesCreate(newUser);
        } else if (oldUser != null && newUser == null) {
            // DELETION
            return buildUserChangesDelete(oldUser);
        } else {
            // MODIFICATION
            return buildUserChanges(oldUser, newUser);
        }
    }

    private List<AuditChange> buildUserChanges(User oldUser, User newUser) {
        final List<AuditChange> changes = new ArrayList<>();
        Optional.ofNullable(buildChange(oldUser.login(), newUser.login(), "login")).map(changes::add);
        Optional.ofNullable(buildChange(oldUser.firstName(), newUser.firstName(), "firstName")).map(changes::add);
        Optional.ofNullable(buildChange(oldUser.lastName(), newUser.lastName(), "lastName")).map(changes::add);
        Optional.ofNullable(buildChange(oldUser.email(), newUser.email(), "email")).map(changes::add);
        Optional.ofNullable(buildChange(oldUser.middleName(), newUser.middleName(), "middleName")).map(changes::add);
        Optional.ofNullable(buildChange(oldUser.notifEmail(), newUser.notifEmail(), "notifEmail")).map(changes::add);

        if (!oldUser.status().equals(newUser.status())) {
            changes.add(AuditChange.builder()
                    .from(oldUser.status().name())
                    .to(newUser.status().name())
                    .object("status")
                    .operation(AuditOperation.UPDATE)
                    .build());
        }

        return changes;
    }

    private List<AuditChange> buildUserChangesDelete(User oldUser) {
        return List.of(
                AuditChange.builder()
                        .from(oldUser.login())
                        .object("login")
                        .operation(AuditOperation.REMOVE)
                        .build(),
                AuditChange.builder()
                        .from(oldUser.firstName())
                        .object("firstName")
                        .operation(AuditOperation.REMOVE)
                        .build(),
                AuditChange.builder()
                        .from(oldUser.lastName())
                        .object("lastName")
                        .operation(AuditOperation.REMOVE)
                        .build(),
                AuditChange.builder()
                        .from(oldUser.email())
                        .object("email")
                        .operation(AuditOperation.REMOVE)
                        .build(),
                AuditChange.builder()
                        .from(oldUser.notifEmail())
                        .object("notifEmail")
                        .operation(AuditOperation.REMOVE)
                        .build(),
                AuditChange.builder()
                        .from(oldUser.middleName())
                        .object("middleName")
                        .operation(AuditOperation.REMOVE)
                        .build(),
                AuditChange.builder()
                        .to(oldUser.status().getValue().toString())
                        .object("status")
                        .operation(AuditOperation.REMOVE)
                        .build());
    }

    private List<AuditChange> buildUserChangesCreate(User newUser) {
        return List.of(
                AuditChange.builder()
                        .to(newUser.login())
                        .object("login")
                        .operation(AuditOperation.ADD)
                        .build(),
                AuditChange.builder()
                        .to(newUser.firstName())
                        .object("firstName")
                        .operation(AuditOperation.ADD)
                        .build(),
                AuditChange.builder()
                        .to(newUser.lastName())
                        .object("lastName")
                        .operation(AuditOperation.ADD)
                        .build(),
                AuditChange.builder()
                        .to(newUser.email())
                        .object("email")
                        .operation(AuditOperation.ADD)
                        .build(),
                AuditChange.builder()
                        .to(newUser.notifEmail())
                        .object("notifEmail")
                        .operation(AuditOperation.ADD)
                        .build(),
                AuditChange.builder()
                        .to(newUser.middleName())
                        .object("middleName")
                        .operation(AuditOperation.ADD)
                        .build(),
                AuditChange.builder()
                        .to(newUser.status().getValue().toString())
                        .object("status")
                        .operation(AuditOperation.ADD)
                        .build()
        );
    }
}
