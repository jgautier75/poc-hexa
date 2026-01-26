package com.acme.jga.search.filtering.parser;

import com.acme.jga.search.filtering.expr.ExpressionType;
import com.acme.jga.search.filtering.expr.FilterComparison;
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

    @Test
    public void Query_Parse_Complex() {
        String rawQuery = "(name eq 'toto' and login ne 'm') or (name eq 'tata' and age gt '25')";
        ParsingResult parsingResult = QRY_PARSER.parseQuery(rawQuery);
        assertNotNull(parsingResult, "Parsing result not null");
        assertTrue(parsingResult.getErrorNodes().isEmpty(), "No errors");
        assertEquals(19, parsingResult.getExpressions().size(), "19 expressions");
        assertEquals(parsingResult.getExpressions().get(0).getType(), ExpressionType.OPENING_PARENTHESIS, "Opening parenthesis");

        assertEquals(parsingResult.getExpressions().get(1).getType(), ExpressionType.PROPERTY, "First Property match");
        assertEquals(parsingResult.getExpressions().get(1).getValue(),"name","First property named \"name\"");

        assertEquals(parsingResult.getExpressions().get(2).getType(), ExpressionType.COMPARISON, "First operator match");
        assertEquals(FilterComparison.fromValueParam(parsingResult.getExpressions().get(2).getValue()), FilterComparison.EQUALS, "First operator is equals");

        assertEquals(parsingResult.getExpressions().get(3).getType(), ExpressionType.VALUE, "First value match");
        assertEquals(parsingResult.getExpressions().get(3).getValue(), "'toto'", "First value 'toto'");

        assertEquals(parsingResult.getExpressions().get(4).getType(), ExpressionType.OPERATOR, "First operator match");
        assertEquals(parsingResult.getExpressions().get(4).getValue(), "and", "First operator value match");
    }

}