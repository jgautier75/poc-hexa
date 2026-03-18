package com.acme.jga.rest.filters;

import com.acme.jga.utils.HttpUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class RequestDebugFilter extends OncePerRequestFilter {
    private static final String DEBUG_PARAM = "X-APP-DEBUG";
    public final static ScopedValue<Boolean> DEBUG_REQ = ScopedValue.newInstance();
    private static final Logger LOG = LoggerFactory.getLogger(RequestDebugFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean debugMode = Optional.ofNullable(request.getHeader(DEBUG_PARAM)).isPresent()
                || Optional.ofNullable(request.getParameter(DEBUG_PARAM)).isPresent();
        ScopedValue.where(DEBUG_REQ, debugMode).run(() -> {
            try {
                if (DEBUG_REQ.get() != null && DEBUG_REQ.get()) {
                    String httpReqDump = HttpUtils.dumpHttpRequest(request);
                    LOG.info(">>>>>> HTTP REQUEST >>>>> {}", httpReqDump);
                }
                filterChain.doFilter(request, response);
            } catch (IOException | ServletException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
