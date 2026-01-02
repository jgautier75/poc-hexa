package com.acme.jga.domain.search;

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

    public static Map<SearchParams, Object> checkParameters(Map<SearchParams, Object> searchParams) {
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
                }
            }
            if (!searchParams.containsKey(SearchParams.PAGE_INDEX)) {
                params.put(SearchParams.PAGE_INDEX, DEFAULT_PAGE_INDEX);
            } else {
                if (((Integer) searchParams.get(SearchParams.PAGE_INDEX)) <= 0) {
                    params.put(SearchParams.PAGE_INDEX, DEFAULT_PAGE_INDEX);
                }
            }
        }
        ParsingResult parsingResult = QUERY_PARSER.parseQuery((String) searchParams.get(SearchParams.FILTER));
        params.put(SearchParams.PARSING_RESULTS, parsingResult);
        return params;
    }

}
