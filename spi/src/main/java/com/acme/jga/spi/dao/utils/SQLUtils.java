package com.acme.jga.spi.dao.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.Normalizer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SQLUtils {

    public static String diacritic(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll(" ", "")
                .replaceAll("[^A-Za-z0-9]","")
                .toLowerCase();
    }

}
