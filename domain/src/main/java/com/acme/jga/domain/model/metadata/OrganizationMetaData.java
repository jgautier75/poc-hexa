package com.acme.jga.domain.model.metadata;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum OrganizationMetaData {
    UID("uid", "uid", DataType.STRING, false),
    UUID("uuid", "uid", DataType.STRING, false),
    LABEL("label", "search_label", DataType.STRING, true),
    CODE("code", "code", DataType.STRING, false),
    COUNTRY("country", "country", DataType.STRING, false),
    KIND("kind", "kind", DataType.ENUM_NUMBER, false),
    ;

    private final String alias;
    private final String columnName;
    private final DataType dataType;
    private final boolean diacritic;

    OrganizationMetaData(String alias, String columnName, DataType dataType, boolean diacritic) {
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

    public static Map<String, EntityMetaData> columnsByAlias() {
        final Map<String, EntityMetaData> colsByAlias = new HashMap<>();
        Arrays.stream(OrganizationMetaData.values()).forEach(userMetaData -> colsByAlias.put(userMetaData.getAlias(),
                new EntityMetaData(userMetaData.getColumnName(), userMetaData.getDataType().name(), false)));
        return colsByAlias;
    }
}
