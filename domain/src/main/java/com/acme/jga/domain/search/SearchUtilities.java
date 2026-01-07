package com.acme.jga.domain.search;

import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.exceptions.Scope;
import com.acme.jga.domain.i18n.BundleFactory;
import com.acme.jga.search.filtering.constants.SearchParams;
import com.acme.jga.search.filtering.parser.QueryParser;
import com.acme.jga.search.filtering.utils.ParsingResult;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class SearchUtilities {
    private static final Integer DEFAULT_PAGE_SIZE = 25;
    private static final Integer MAX_PAGE_SIZE = 100;
    private static final Integer DEFAULT_PAGE_INDEX = 1;
    private static final QueryParser QUERY_PARSER = new QueryParser();

    public static Map<SearchParams, Object> checkParameters(Map<SearchParams, Object> searchParams) throws FunctionalException {
        HashMap<SearchParams, Object> params = new HashMap<>();
        if (searchParams == null || searchParams.isEmpty()) {
            params.put(SearchParams.PAGE_SIZE, DEFAULT_PAGE_SIZE);
            params.put(SearchParams.PAGE_INDEX, DEFAULT_PAGE_INDEX);
        } else {
            if (!searchParams.containsKey(SearchParams.PAGE_SIZE)) {
                params.put(SearchParams.PAGE_SIZE, DEFAULT_PAGE_SIZE);
            } else {
                if (((Integer) searchParams.get(SearchParams.PAGE_SIZE)) > MAX_PAGE_SIZE) {
                    params.put(SearchParams.PAGE_SIZE, DEFAULT_PAGE_SIZE);
                } else {
                    params.put(SearchParams.PAGE_SIZE, searchParams.get(SearchParams.PAGE_SIZE));
                }
            }
            if (!searchParams.containsKey(SearchParams.PAGE_INDEX)) {
                params.put(SearchParams.PAGE_INDEX, DEFAULT_PAGE_INDEX);
            } else {
                if (((Integer) searchParams.get(SearchParams.PAGE_INDEX)) <= 0) {
                    params.put(SearchParams.PAGE_INDEX, DEFAULT_PAGE_INDEX);
                } else {
                    params.put(SearchParams.PAGE_INDEX, searchParams.get(SearchParams.PAGE_INDEX));
                }
            }
            if (searchParams.containsKey(SearchParams.ORDER_BY)) {
                params.put(SearchParams.ORDER_BY, searchParams.get(SearchParams.ORDER_BY));
            }
        }
        ParsingResult parsingResult = QUERY_PARSER.parseQuery((String) searchParams.get(SearchParams.FILTER));
        params.put(SearchParams.PARSING_RESULTS, parsingResult);
        if (!parsingResult.getErrorNodes().isEmpty()) {
            final StringBuilder sb = new StringBuilder();
            parsingResult.getErrorNodes().forEach(en -> sb.append(en.toString()));
            throw new FunctionalException(Scope.REQUEST.name(), FunctionalErrors.INVALID_PROPERTY.name(), BundleFactory.getMessage("filter_invalid", sb.toString()));
        }
        return params;
    }

    public static Map<SearchParams, Object> getDefaultParameters() {
        HashMap<SearchParams, Object> params = new HashMap<>();
        params.put(SearchParams.PAGE_SIZE, DEFAULT_PAGE_SIZE);
        params.put(SearchParams.PAGE_INDEX, DEFAULT_PAGE_INDEX);
        return params;
    }

}
