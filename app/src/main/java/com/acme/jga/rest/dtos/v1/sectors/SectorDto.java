package com.acme.jga.rest.dtos.v1.sectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class SectorDto {
    private String code;
    private String label;
    private String parentUid;
}
