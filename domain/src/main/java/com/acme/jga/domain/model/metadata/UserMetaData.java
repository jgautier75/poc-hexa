package com.acme.jga.domain.model.metadata;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum UserMetaData {
    UID("uid", "uid", DataType.STRING, false),
    UUID("uuid", "uid", DataType.STRING, false),
    FIRST_NAME("firstName", "search_first_name", DataType.STRING, true),
    LAST_NAME("lastName", "search_last_name", DataType.STRING, true),
    LOGIN("login", "login", DataType.STRING, false),
    EMAIL("email", "email", DataType.STRING, false),
    ;

    private final String alias;
    private final String columnName;
    private final DataType dataType;
    private boolean diacritic;

    UserMetaData(String alias, String columnName, DataType dataType, boolean diacritic) {
        this.alias = alias;
        this.columnName = columnName;
        this.dataType = dataType;
        this.diacritic = diacritic;
    }

    public DataType getDataType() {
        return dataType;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getAlias() {
        return alias;
    }

    public boolean isDiacritic() {
        return diacritic;
    }

    public static Map<String, KeyValuePair> columnsByAlias() {
        final Map<String, KeyValuePair> colsByAlias = new HashMap<>();
        Arrays.stream(UserMetaData.values()).forEach(userMetaData -> {
            colsByAlias.put(userMetaData.getAlias(), new KeyValuePair(userMetaData.getColumnName(), userMetaData.getDataType().name(), userMetaData.isDiacritic()));
        });
        return colsByAlias;
    }

}
