package com.acme.jga.spi.dao.processors;

import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.exceptions.WrappedFunctionalException;
import com.acme.jga.domain.model.metadata.EntityMetaData;
import com.acme.jga.domain.model.metadata.OrganizationMetaData;
import com.acme.jga.domain.model.organization.OrganizationKind;
import com.acme.jga.search.filtering.constants.SearchParams;
import com.acme.jga.search.filtering.expr.Expression;
import com.acme.jga.search.filtering.expr.FilterComparison;
import com.acme.jga.search.filtering.utils.ParsingResult;
import com.acme.jga.spi.dao.utils.AbstractJdbcDaoSupport;
import com.acme.jga.spi.dao.utils.DaoConstants;
import com.acme.jga.spi.dao.utils.SQLUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class ExpressionsHandler {
    public AbstractJdbcDaoSupport.CompositeQuery buildFilterQuery(Map<String, Object> sqlParams, Map<SearchParams, Object> searchParams, Map<String, EntityMetaData> domainEntityMetaData) {
        AbstractJdbcDaoSupport.PaginationResult pagination = computePagination(searchParams, domainEntityMetaData);
        if (!hasExpressions(searchParams)) {
            return emptyQuery(sqlParams, pagination);
        }
        ParsingResult parsingResult = getParsingResult(searchParams);
        String sql = buildSqlExpression(parsingResult.getExpressions(), sqlParams, domainEntityMetaData);
        return new AbstractJdbcDaoSupport.CompositeQuery(true, sql, sqlParams, pagination.pagination(), pagination.orderBy());
    }

    private boolean hasExpressions(Map<SearchParams, Object> searchParams) {
        if (searchParams == null || searchParams.isEmpty()) {
            return false;
        }
        ParsingResult parsingResult = getParsingResult(searchParams);
        return parsingResult != null && !CollectionUtils.isEmpty(parsingResult.getExpressions());
    }

    private ParsingResult getParsingResult(Map<SearchParams, Object> searchParams) {
        if (searchParams == null) {
            return null;
        }
        return (ParsingResult) searchParams.get(SearchParams.PARSING_RESULTS);
    }

    private AbstractJdbcDaoSupport.CompositeQuery emptyQuery(Map<String, Object> sqlParams, AbstractJdbcDaoSupport.PaginationResult pagination) {
        return new AbstractJdbcDaoSupport.CompositeQuery(false, "", sqlParams, pagination.pagination(), pagination.orderBy());
    }

    private String buildSqlExpression(List<Expression> expressions, Map<String, Object> sqlParams, Map<String, EntityMetaData> domainEntityMetaData) {
        StringBuilder sql = new StringBuilder();
        ExpressionContext context = new ExpressionContext();
        for (int index = 0; index < expressions.size(); index++) {
            Expression expression = expressions.get(index);
            appendExpression(sql, expression, index, context, sqlParams, domainEntityMetaData);
        }
        return sql.toString();
    }

    private void appendExpression(StringBuilder sql, Expression expression, int index, ExpressionContext context, Map<String, Object> sqlParams, Map<String, EntityMetaData> domainEntityMetaData) {
        switch (expression.getType()) {
            case OPENING_PARENTHESIS -> sql.append("(");
            case CLOSING_PARENTEHSIS -> sql.append(")");
            case NEGATION -> sql.append(" not ");
            case OPERATOR -> sql.append(" ").append(getSqlParam(expression)).append(" ");
            case COMPARISON -> {
                context.likeOperator = isLikeOperator(expression);
                sql.append(" ").append(getSqlParam(expression)).append(" ");
            }
            case PROPERTY -> appendProperty(sql, expression, index, context, domainEntityMetaData);
            case VALUE -> appendValue(sql, expression, context, sqlParams);
        }
    }

    private void appendProperty(StringBuilder sql, Expression expression, int index, ExpressionContext context, Map<String, EntityMetaData> domainEntityMetaData) {
        String propertyName = stripEnclosingQuotes(expression.getValue());
        EntityMetaData metadata = getPropertyMetadata(domainEntityMetaData, propertyName);
        context.parameterName = "p" + propertyName + index;
        context.diacritic = metadata.isDiacritic();
        sql.append(metadata.getKey());
    }

    private void appendValue(StringBuilder sql, Expression expression, ExpressionContext context, Map<String, Object> sqlParams) {
        String value = stripEnclosingQuotes(expression.getValue());
        sql.append(":").append(context.parameterName);
        addToSQLParams(sqlParams, context.parameterName, value, context.diacritic, context.likeOperator);
        context.likeOperator = false;
    }

    private EntityMetaData getPropertyMetadata(Map<String, EntityMetaData> domainEntityMetaData, String propertyName) {
        EntityMetaData metadata = domainEntityMetaData.get(propertyName);
        if (metadata == null) {
            throw invalidProperty("Unmapped property named [" + propertyName + "]");
        }
        return metadata;
    }

    private static void addToSQLParams(Map<String, Object> sqlParams, String parameterName, String value, boolean diacritic, boolean likeOperator) {
        if (OrganizationMetaData.KIND.getAlias().equalsIgnoreCase(parameterName)) {
            sqlParams.put(parameterName, OrganizationKind.valueOf(value).getValue());
            return;
        }
        String normalizedValue = diacritic ? SQLUtils.diacritic(value) : value;
        if (likeOperator) {
            normalizedValue = normalizedValue.replace("*", "%");
        }
        sqlParams.put(parameterName, normalizedValue);
    }

    /**
     * Strip enclosing single quotes.
     */
    public String stripEnclosingQuotes(String value) {
        if (value != null && value.startsWith("'") && value.endsWith("'")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    /**
     * Translate filter expression to SQL expression.
     */
    private String getSqlParam(Expression expression) {
        return Objects.requireNonNull(FilterComparison.fromValueParam(expression.getValue())).getSqlParam();
    }

    private boolean isLikeOperator(Expression expression) {
        return FilterComparison.LIKE == FilterComparison.fromValueParam(expression.getValue());
    }

    /**
     * Is column a diacritic search.
     */
    private boolean isDiacritic(Map<String, EntityMetaData> domainEntityMetaData, String propertyName) {
        EntityMetaData metadata = domainEntityMetaData.get(propertyName);
        return metadata != null && metadata.isDiacritic();
    }

    /**
     * Compute pagination and orderBy SQL instructions.
     */
    private AbstractJdbcDaoSupport.PaginationResult computePagination(Map<SearchParams, Object> searchParams, Map<String, EntityMetaData> columnsDefinitionsByAlias) {
        int pageIndex = getPageIndex(searchParams);
        int pageSize = getPageSize(searchParams);
        int start = (pageIndex - 1) * pageSize;
        String pagination = String.format(DaoConstants.PAGINATION_PATTERN, pageSize, start);
        String orderBy = getOrderBy(searchParams, columnsDefinitionsByAlias);
        return new AbstractJdbcDaoSupport.PaginationResult(pagination, orderBy);
    }

    private int getPageIndex(Map<SearchParams, Object> searchParams) {
        return Optional.ofNullable((Integer) searchParams.get(SearchParams.PAGE_INDEX)).filter(index -> index > 0).orElse(1);
    }

    private int getPageSize(Map<SearchParams, Object> searchParams) {
        return Optional.ofNullable((Integer) searchParams.get(SearchParams.PAGE_SIZE)).orElse(DaoConstants.DEFAULT_PAGE_SIZE);
    }

    /**
     * Build orderBy instruction.
     */
    private String getOrderBy(Map<SearchParams, Object> searchParams, Map<String, EntityMetaData> columnsDefinitionsByAlias) {
        String orderByParam = (String) searchParams.get(SearchParams.ORDER_BY);
        if (orderByParam == null || orderByParam.isEmpty()) {
            return "";
        }
        String orderDirection = orderByParam.substring(0, 1);
        if (!isValidOrderDirection(orderDirection)) {
            return "";
        }
        String orderColumn = orderByParam.substring(1);
        EntityMetaData metadata = columnsDefinitionsByAlias.get(orderColumn);
        if (metadata == null) {
            throw invalidProperty("Unknown orderBy named [" + orderColumn + "]");
        }
        String direction = DaoConstants.ORDER_ASC_SIGN.equals(orderDirection) ? DaoConstants.ORDER_ASC_KEYWORD : DaoConstants.ORDER_DESC_KEYWORD;
        return DaoConstants.ORDER_BY + metadata.getKey() + direction;
    }

    private boolean isValidOrderDirection(String direction) {
        return DaoConstants.ORDER_ASC_SIGN.equals(direction) || DaoConstants.ORDER_DESC_SIGN.equals(direction);
    }

    private WrappedFunctionalException invalidProperty(String message) {
        return new WrappedFunctionalException(new FunctionalException(FunctionalErrors.INVALID_PROPERTY.name(), null, message));
    }

    private static class ExpressionContext {
        private String parameterName;
        private boolean likeOperator;
        private boolean diacritic;
    }
}