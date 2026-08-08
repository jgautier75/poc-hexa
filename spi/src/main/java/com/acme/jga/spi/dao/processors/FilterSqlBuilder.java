package com.acme.jga.spi.dao.processors;

import com.acme.jga.domain.model.metadata.EntityMetaData;
import com.acme.jga.domain.model.metadata.OrganizationMetaData;
import com.acme.jga.domain.model.organization.OrganizationKind;
import com.acme.jga.search.filtering.expr.Expression;
import com.acme.jga.search.filtering.expr.FilterComparison;
import com.acme.jga.spi.dao.utils.SQLUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class FilterSqlBuilder {
    private final PropertyMetadataResolver metadataResolver;

    public FilterSqlBuilder(PropertyMetadataResolver metadataResolver) {
        this.metadataResolver = metadataResolver;
    }

    public String build(List<Expression> expressions, Map<String, Object> sqlParams, Map<String, EntityMetaData> metadata) {
        StringBuilder sql = new StringBuilder();
        ExpressionContext context = new ExpressionContext();
        for (int index = 0; index < expressions.size(); index++) {
            appendExpression(sql, expressions.get(index), index, context, sqlParams, metadata);
        }
        return sql.toString();
    }

    private void appendExpression(
            StringBuilder sql,
            Expression expression,
            int index,
            ExpressionContext context,
            Map<String, Object> sqlParams,
            Map<String, EntityMetaData> metadata) {
        switch (expression.getType()) {
            case OPENING_PARENTHESIS -> sql.append("(");
            case CLOSING_PARENTEHSIS -> sql.append(")");
            case NEGATION -> sql.append(" not ");
            case OPERATOR -> appendSqlOperator(sql, expression);
            case COMPARISON -> appendComparison(sql, expression, context);
            case PROPERTY -> appendProperty(sql, expression, index, context, metadata);
            case VALUE -> appendValue(sql, expression, context, sqlParams);
        }
    }

    private void appendSqlOperator(StringBuilder sql, Expression expression) {
        sql.append(" ").append(getSqlParam(expression)).append(" ");
    }

    private void appendComparison(StringBuilder sql, Expression expression, ExpressionContext context) {
        FilterComparison comparison = getComparison(expression);
        context.likeOperator = FilterComparison.LIKE == comparison;
        sql.append(" ").append(comparison.getSqlParam()).append(" ");
    }

    private void appendProperty(StringBuilder sql, Expression expression, int index, ExpressionContext context, Map<String, EntityMetaData> metadata) {
        String propertyName = stripEnclosingQuotes(expression.getValue());
        EntityMetaData propertyMetadata = metadataResolver.resolve(metadata, propertyName);
        context.parameterName = "p" + propertyName + index;
        context.diacritic = propertyMetadata.isDiacritic();
        sql.append(propertyMetadata.getKey());
    }

    private void appendValue(StringBuilder sql, Expression expression, ExpressionContext context, Map<String, Object> sqlParams) {
        String value = stripEnclosingQuotes(expression.getValue());
        sql.append(":").append(context.parameterName);
        addParameter(sqlParams, context.parameterName, value, context.diacritic, context.likeOperator);
        context.likeOperator = false;
    }

    private void addParameter(Map<String, Object> sqlParams, String parameterName, String value, boolean diacritic, boolean likeOperator) {
        if (isOrganizationKind(parameterName)) {
            sqlParams.put(parameterName, OrganizationKind.valueOf(value).getValue());
            return;
        }
        String normalizedValue = diacritic ? SQLUtils.diacritic(value) : value;
        if (likeOperator) {
            normalizedValue = normalizedValue.replace("*", "%");
        }
        sqlParams.put(parameterName, normalizedValue);
    }

    private boolean isOrganizationKind(String parameterName) {
        return OrganizationMetaData.KIND.getAlias().equalsIgnoreCase(parameterName);
    }

    private FilterComparison getComparison(Expression expression) {
        return Objects.requireNonNull(FilterComparison.fromValueParam(expression.getValue()));
    }

    private String getSqlParam(Expression expression) {
        return getComparison(expression).getSqlParam();
    }

    private String stripEnclosingQuotes(String value) {
        if (value != null && value.startsWith("'") && value.endsWith("'")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    private static class ExpressionContext {
        private String parameterName;
        private boolean likeOperator;
        private boolean diacritic;
    }
}

