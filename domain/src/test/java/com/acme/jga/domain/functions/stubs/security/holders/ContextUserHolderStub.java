package com.acme.jga.domain.functions.stubs.security.holders;

import com.acme.jga.domain.security.holders.ContextUserHolder;

public class ContextUserHolderStub implements ContextUserHolder {
    @Override
    public String getCurrentUser() {
        return "anonymous";
    }
}
