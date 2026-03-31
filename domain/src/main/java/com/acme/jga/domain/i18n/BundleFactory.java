package com.acme.jga.domain.i18n;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public enum BundleFactory {
    ;
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("messages");

    public static String getMessage(String key, Object... args) {
        String targetMsg = key;
        try {
            String msg = BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            // Resource not found in bundle
        }
        return String.format(targetMsg, args);
    }
}
