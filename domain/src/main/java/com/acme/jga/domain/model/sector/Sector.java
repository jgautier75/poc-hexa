package com.acme.jga.domain.model.sector;

import com.acme.jga.domain.model.generic.CompositeId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class Sector {

    private CompositeId id;
    private CompositeId tenantId;
    private CompositeId organizationId;
    private String label;
    private String code;
    private CompositeId parent;
    private boolean root;

    private List<Sector> children = new ArrayList<>();

    public void addChild(Sector sector) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(sector);
    }

}
