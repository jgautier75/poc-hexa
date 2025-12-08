package com.acme.jga.domain.i18n;

import java.util.ResourceBundle;

public enum BundleFactory {
    ;
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("messages");

    public static String getMessage(String key, Object... args) {
        return String.format(BUNDLE.getString(key), args);
    }
}
