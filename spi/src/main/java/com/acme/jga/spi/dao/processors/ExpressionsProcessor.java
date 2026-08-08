package com.acme.jga.spi.dao.processors;

import com.acme.jga.domain.model.metadata.EntityMetaData;
import com.acme.jga.search.filtering.constants.SearchParams;
import com.acme.jga.search.filtering.utils.ParsingResult;
import com.acme.jga.spi.dao.utils.AbstractJdbcDaoSupport;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ExpressionsProcessor {

    private final FilterSqlBuilder filterSqlBuilder;
    private final PaginationBuilder paginationBuilder;

    public ExpressionsProcessor(
            FilterSqlBuilder filterSqlBuilder,
            PaginationBuilder paginationBuilder) {
        this.filterSqlBuilder = filterSqlBuilder;
        this.paginationBuilder = paginationBuilder;
    }

    public AbstractJdbcDaoSupport.CompositeQuery buildFilterQuery(
            Map<String, Object> sqlParams,
            Map<SearchParams, Object> searchParams,
            Map<String, EntityMetaData> domainEntityMetaData) {
        AbstractJdbcDaoSupport.PaginationResult pagination = paginationBuilder.build(searchParams, domainEntityMetaData);
        ParsingResult parsingResult = getParsingResult(searchParams);
        if (!hasExpressions(parsingResult)) {
            return new AbstractJdbcDaoSupport.CompositeQuery(false, "", sqlParams, pagination.pagination(), pagination.orderBy());
        }
        String sql = filterSqlBuilder.build(parsingResult.getExpressions(), sqlParams, domainEntityMetaData);
        return new AbstractJdbcDaoSupport.CompositeQuery(true, sql, sqlParams, pagination.pagination(), pagination.orderBy());
    }

    private ParsingResult getParsingResult(Map<SearchParams, Object> searchParams) {
        if (searchParams == null) {
            return null;
        }
        return (ParsingResult) searchParams.get(SearchParams.PARSING_RESULTS);
    }

    private boolean hasExpressions(ParsingResult parsingResult) {
        return parsingResult != null && parsingResult.getExpressions() != null && !parsingResult.getExpressions().isEmpty();
    }
}
