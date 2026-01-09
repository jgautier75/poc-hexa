package com.acme.jga.domain.functions.events.builders;

import com.acme.jga.domain.model.event.AuditChange;
import com.acme.jga.domain.model.event.AuditOperation;

public abstract class AbstractEventBuilder {
    protected AuditChange buildChange(String from, String to, String field) {
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
            if (!from.equals(to)) {
                return AuditChange.builder()
                        .from(from)
                        .to(to)
                        .object(field)
                        .operation(AuditOperation.UPDATE)
                        .build();
            }
            return null;
        }
    }
}
