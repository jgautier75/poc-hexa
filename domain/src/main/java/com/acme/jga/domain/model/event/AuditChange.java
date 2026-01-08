package com.acme.jga.domain.model.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class AuditChange {
    private String object;
    private AuditOperation operation;
    private String from;
    private String to;
}
