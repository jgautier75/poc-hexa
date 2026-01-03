package com.acme.jga.search.filtering.parser;

import com.acme.jga.search.filtering.antlr.FilterLexer;
import com.acme.jga.search.filtering.antlr.FilterParser;
import com.acme.jga.search.filtering.listener.AntlrErrorListener;
import com.acme.jga.search.filtering.listener.SearchFilterListener;
import com.acme.jga.search.filtering.utils.ParsingResult;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.Collections;

public class QueryParser {

    public ParsingResult parseQuery(String query) {
        if (query == null || query.isEmpty()) {
            return new ParsingResult(Collections.emptyList(), Collections.emptyList(), true);
        }
        CodePointCharStream stream = CharStreams.fromString(query);
        FilterLexer lexer = new FilterLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        FilterParser parser = new FilterParser(tokens);
        parser.removeErrorListeners();
        AntlrErrorListener antlrErrorListener = new AntlrErrorListener();
        parser.addErrorListener(antlrErrorListener);
        SearchFilterListener searchFilterListener = new SearchFilterListener();
        ParseTreeWalker.DEFAULT.walk(searchFilterListener, parser.filter());
        return new ParsingResult(searchFilterListener.getExpressions(), searchFilterListener.getErrors(), false);
    }

}
