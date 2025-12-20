package com.acme.jga.rest.dtos.v1.sectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class SectorDisplayDto {
    private String uid;
    private String code;
    private String label;
    private List<SectorDisplayDto> children;

    public void addChild(SectorDisplayDto childSector) {
        if (children == null) {
            this.children = new ArrayList<>();
        }
        children.add(childSector);
    }
}
