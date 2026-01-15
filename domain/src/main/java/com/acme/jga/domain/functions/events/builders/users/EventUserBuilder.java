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
            return buildUserChanges(newUser, AuditOperation.ADD);
        } else if (oldUser != null && newUser == null) {
            // DELETION
            return buildUserChanges(oldUser, AuditOperation.REMOVE);
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

        if (oldUser.status().equals(newUser.status())) {
            changes.add(AuditChange.builder()
                    .from(oldUser.status().name())
                    .to(newUser.status().name())
                    .object("status")
                    .operation(AuditOperation.UPDATE)
                    .build());
        }

        return changes;
    }

    private List<AuditChange> buildUserChanges(User newUser, AuditOperation operation) {
        return List.of(
                AuditChange.builder()
                        .to(newUser.login())
                        .object("login")
                        .operation(operation)
                        .build(),
                AuditChange.builder()
                        .to(newUser.firstName())
                        .object("firstName")
                        .operation(operation)
                        .build(),
                AuditChange.builder()
                        .to(newUser.lastName())
                        .object("lastName")
                        .operation(operation)
                        .build(),
                AuditChange.builder()
                        .to(newUser.email())
                        .object("email")
                        .operation(operation)
                        .build(),
                AuditChange.builder()
                        .object("notifEmail")
                        .operation(operation)
                        .to(newUser.notifEmail())
                        .build(),
                AuditChange.builder()
                        .object("middleName")
                        .operation(operation)
                        .to(newUser.middleName())
                        .build(),
                AuditChange.builder()
                        .object("status")
                        .operation(operation)
                        .to(newUser.status().getValue().toString())
                        .build()
        );
    }
}
