package com.acme.jga.spi.dao.processors;

import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.exceptions.WrappedFunctionalException;
import com.acme.jga.domain.model.metadata.DataType;
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
import java.util.function.Predicate;

@Component
public class ExpressionsHandler {

    public AbstractJdbcDaoSupport.CompositeQuery buildFilterQuery(Map<String, Object> sqlParams, Map<SearchParams, Object> searchParams, Map<String, EntityMetaData> domainEntityMetaData) {
        AbstractJdbcDaoSupport.PaginationResult paginationResult = computePagination(searchParams, domainEntityMetaData);
        StringBuilder sqlQuery = new StringBuilder();
        if (searchParams==null || searchParams.isEmpty()) {
            return new AbstractJdbcDaoSupport.CompositeQuery(false, sqlQuery.toString(), sqlParams, paginationResult.pagination(), paginationResult.orderBy());
        }

        ParsingResult parsingResult = (ParsingResult) searchParams.get(SearchParams.PARSING_RESULTS);
        if (searchParams.get(SearchParams.PARSING_RESULTS) ==null){
            return new AbstractJdbcDaoSupport.CompositeQuery(false, sqlQuery.toString(), sqlParams, paginationResult.pagination(), paginationResult.orderBy());
        }

        Predicate<ParsingResult> hasExpressions = (pr) -> !CollectionUtils.isEmpty(pr.getExpressions());
        if (!hasExpressions.test(parsingResult)) {
            return new AbstractJdbcDaoSupport.CompositeQuery(false, sqlQuery.toString(), sqlParams, paginationResult.pagination(), paginationResult.orderBy());
        }

        int index = 0;
        String sqlParameterName = null;
        boolean isLikeOperator = false;
        boolean isDiacritic = false;
        List<Expression> expressions = parsingResult.getExpressions();
        for (Expression expression : expressions) {
            switch (expression.getType()) {
                case OPENING_PARENTHESIS -> sqlQuery.append("(");
                case COMPARISON -> {
                    isLikeOperator = isLikeOperator(expression);
                    sqlQuery.append(" ").append(getSqlParam(expression)).append(" ");
                }
                case CLOSING_PARENTEHSIS -> sqlQuery.append(")");
                case NEGATION -> sqlQuery.append(" not ");
                case OPERATOR -> sqlQuery.append(" ").append(getSqlParam(expression)).append(" ");
                case PROPERTY -> {
                    String paramName = stripEnclosingQuotes(expression.getValue());
                    sqlParameterName = "p" + paramName + index;
                    isDiacritic = isDiacritic(domainEntityMetaData, paramName);
                    EntityMetaData columnNameAndColumnType = domainEntityMetaData.get(paramName);
                    if (columnNameAndColumnType == null) {
                        throw new WrappedFunctionalException(new FunctionalException(FunctionalErrors.INVALID_PROPERTY.name(), null, "Unmapped property named [" + paramName + "]"));
                    }
                    sqlQuery.append(columnNameAndColumnType.getKey());
                }
                case VALUE -> {
                    String value = stripEnclosingQuotes(expression.getValue());
                    sqlQuery.append(":").append(sqlParameterName);
                    addToSQLParams(sqlParams, sqlParameterName, value, isDiacritic, isLikeOperator);
                    isLikeOperator = false;
                }
            }
            index++;
        }
        return new AbstractJdbcDaoSupport.CompositeQuery(hasExpressions.test(parsingResult), sqlQuery.toString(), sqlParams, paginationResult.pagination(), paginationResult.orderBy());
    }

    private static void addToSQLParams(Map<String, Object> sqlParams, String sqlParameterName, String value, boolean isDiacritic, boolean isLikeOperator) {
        if (OrganizationMetaData.KIND.getAlias().equalsIgnoreCase(sqlParameterName)) {
            sqlParams.put(sqlParameterName, OrganizationKind.valueOf(value).getValue());
        } else {
            String defaultValue = value;
            if (isDiacritic) {
                defaultValue = SQLUtils.diacritic(value);
            }
            sqlParams.put(sqlParameterName, isLikeOperator ? defaultValue.replace("*", "%") : defaultValue);
        }
    }

    /**
     * Strip enclosing single quotes.
     *
     * @param value Value
     * @return Stripped value
     */
    public String stripEnclosingQuotes(String value) {
        if (value != null && value.startsWith("'") && value.endsWith("'")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    /**
     * Translate filter expression to SQL expression.
     *
     * @param expression Expression
     * @return SQL expression
     */
    private String getSqlParam(Expression expression) {
        return Objects.requireNonNull(FilterComparison.fromValueParam(expression.getValue())).getSqlParam();
    }

    private boolean isLikeOperator(Expression expression) {
        return FilterComparison.LIKE == FilterComparison.fromValueParam(expression.getValue());
    }

    @SuppressWarnings("unused")
    private boolean isNumberType(Map<String, EntityMetaData> domainEntityMetaData, String propertyName) {
        EntityMetaData columnNameAndColumnType = domainEntityMetaData.get(propertyName);
        return columnNameAndColumnType != null && DataType.NUMBER.name().equals(columnNameAndColumnType.getValue());
    }

    /**
     * Is column a diacritic search.
     *
     * @param domainEntityMetaData Domain metadata
     * @param propertyName         Property name
     * @return Boolean
     */
    private boolean isDiacritic(Map<String, EntityMetaData> domainEntityMetaData, String propertyName) {
        EntityMetaData columnNameAndColumnType = domainEntityMetaData.get(propertyName);
        return columnNameAndColumnType != null && columnNameAndColumnType.isDiacritic();
    }

    /**
     * Compute pagination and orderBy SQL instructions.
     *
     * @param searchParams Search parameters
     * @return Pagination result
     */
    private AbstractJdbcDaoSupport.PaginationResult computePagination(Map<SearchParams, Object> searchParams, Map<String, EntityMetaData> columnsDefinitionsByAlias) {
        int pageIndex = getPageIndex(searchParams);
        int pageSize = getPageSize(searchParams);
        int start = (pageIndex - 1) * pageSize;

        String pagination = String.format(DaoConstants.PAGINATION_PATTERN, pageSize, start);
        String orderBy = getOrderBy(searchParams, columnsDefinitionsByAlias);

        return new AbstractJdbcDaoSupport.PaginationResult(pagination, orderBy);
    }

    /**
     * Compute page index.
     *
     * @param searchParams Search Parameters
     * @return Page index
     */
    private int getPageIndex(Map<SearchParams, Object> searchParams) {
        return Optional.ofNullable((Integer) searchParams.get(SearchParams.PAGE_INDEX))
                .filter(index -> index > 0)
                .orElse(1);
    }

    /**
     * Compute page size if present, otherwise use default page size.
     *
     * @param searchParams Search parameters
     * @return Page size
     */
    private int getPageSize(Map<SearchParams, Object> searchParams) {
        return Optional.ofNullable((Integer) searchParams.get(SearchParams.PAGE_SIZE))
                .orElse(DaoConstants.DEFAULT_PAGE_SIZE);
    }

    /**
     * Build orderBy instruction.
     *
     * @param searchParams Search parameters
     * @return OrderBy instruction
     */
    private String getOrderBy(Map<SearchParams, Object> searchParams, Map<String, EntityMetaData> columnsDefinitionsByAlias) {
        String orderByParam = (String) searchParams.get(SearchParams.ORDER_BY);
        if (orderByParam == null || orderByParam.isEmpty()) {
            return "";
        }

        String orderDirection = orderByParam.substring(0, 1);
        if (!DaoConstants.ORDER_ASC_SIGN.equals(orderDirection) && !DaoConstants.ORDER_DESC_SIGN.equals(orderDirection)) {
            return "";
        }

        String orderColumn = orderByParam.substring(1);
        EntityMetaData columnNameAndType = columnsDefinitionsByAlias.get(orderColumn);
        if (columnNameAndType == null) {
            throw new WrappedFunctionalException(new FunctionalException(FunctionalErrors.INVALID_PROPERTY.name(), null, "Unknown orderBy named [" + orderColumn + "]"));
        }

        return DaoConstants.ORDER_BY + columnNameAndType.getKey() +
                (DaoConstants.ORDER_ASC_SIGN.equals(orderDirection) ? DaoConstants.ORDER_ASC_KEYWORD : DaoConstants.ORDER_DESC_KEYWORD);
    }

}
