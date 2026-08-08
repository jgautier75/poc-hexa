package com.acme.jga.spi.dao.processors;

import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.exceptions.WrappedFunctionalException;
import com.acme.jga.domain.model.metadata.EntityMetaData;
import com.acme.jga.search.filtering.constants.SearchParams;
import com.acme.jga.spi.dao.utils.AbstractJdbcDaoSupport;
import com.acme.jga.spi.dao.utils.DaoConstants;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class PaginationBuilder {

    public AbstractJdbcDaoSupport.PaginationResult build(Map<SearchParams, Object> searchParams, Map<String, EntityMetaData> metadata) {
        int pageIndex = getPageIndex(searchParams);
        int pageSize = getPageSize(searchParams);
        int start = (pageIndex - 1) * pageSize;
        String pagination = String.format(DaoConstants.PAGINATION_PATTERN, pageSize, start);
        String orderBy = buildOrderBy(searchParams, metadata);
        return new AbstractJdbcDaoSupport.PaginationResult(pagination, orderBy);
    }

    private int getPageIndex(Map<SearchParams, Object> searchParams) {
        return Optional.ofNullable((Integer) searchParams.get(SearchParams.PAGE_INDEX))
                .filter(index -> index > 0)
                .orElse(1);
    }

    private int getPageSize(Map<SearchParams, Object> searchParams) {
        return Optional.ofNullable((Integer) searchParams.get(SearchParams.PAGE_SIZE))
                .orElse(DaoConstants.DEFAULT_PAGE_SIZE);
    }

    private String buildOrderBy(Map<SearchParams, Object> searchParams, Map<String, EntityMetaData> metadata) {
        String orderByParam = (String) searchParams.get(SearchParams.ORDER_BY);
        if (orderByParam == null || orderByParam.isEmpty()) {
            return "";
        }
        String direction = orderByParam.substring(0, 1);
        if (!isValidDirection(direction)) {
            return "";
        }
        String propertyName = orderByParam.substring(1);
        EntityMetaData propertyMetadata = metadata.get(propertyName);
        if (propertyMetadata == null) {throw invalidProperty("Unknown orderBy named [" + propertyName + "]");
        }
        String sqlDirection = DaoConstants.ORDER_ASC_SIGN.equals(direction) ? DaoConstants.ORDER_ASC_KEYWORD : DaoConstants.ORDER_DESC_KEYWORD;
        return DaoConstants.ORDER_BY + propertyMetadata.getKey() + sqlDirection;
    }

    private boolean isValidDirection(String direction) {
        return DaoConstants.ORDER_ASC_SIGN.equals(direction) || DaoConstants.ORDER_DESC_SIGN.equals(direction);
    }

    private WrappedFunctionalException invalidProperty(String message) {
        return new WrappedFunctionalException(new FunctionalException(FunctionalErrors.INVALID_PROPERTY.name(), null, message));
    }
}
