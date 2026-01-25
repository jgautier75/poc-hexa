package com.acme.jga.search.filtering.parser;

import com.acme.jga.search.filtering.expr.ExpressionType;
import com.acme.jga.search.filtering.utils.ParsingResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QueryParserTest {
    private final QueryParser QRY_PARSER = new QueryParser();

    @Test
    public void Query_Parse_Simple() {
        String rawQuery = "login eq 'test'";
        ParsingResult parsingResult = QRY_PARSER.parseQuery(rawQuery);
        assertNotNull(parsingResult, "Parsing result not null");
        assertTrue(parsingResult.getErrorNodes().isEmpty(), "No errors");
        assertEquals(3, parsingResult.getExpressions().size(), "3 expressions");
        boolean foundProperty = parsingResult.getExpressions().stream().anyMatch(exp -> exp.getType().equals(ExpressionType.PROPERTY));
        assertTrue(foundProperty, "Found property expression");
        boolean propertyNameMatch = parsingResult.getExpressions().stream().filter(exp -> exp.getType().equals(ExpressionType.PROPERTY)).findFirst().get().getValue().equals("login");
        assertTrue(propertyNameMatch,"Property named login");
        boolean foundComparison = parsingResult.getExpressions().stream().anyMatch(exp -> exp.getType().equals(ExpressionType.COMPARISON));
        assertTrue(foundComparison, "Found operator expression");
        boolean foundValue = parsingResult.getExpressions().stream().anyMatch(exp -> exp.getType().equals(ExpressionType.VALUE));
        assertTrue(foundValue, "Found value expression");
        boolean valueMatch = parsingResult.getExpressions().stream().filter(exp -> exp.getType().equals(ExpressionType.VALUE)).findFirst().get().getValue().equals("'test'");
        assertTrue(valueMatch,"Value match 'test'");
    }

}