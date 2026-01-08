package com.acme.jga.domain.model.event;

import java.util.List;

public record EventData(String contextUser, String objectUid, AuditScope scope, AuditAction auditAction,
                        EventTarget target, List<AuditChange> auditChanges) {
}
