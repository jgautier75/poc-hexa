package com.acme.jga.utils;

import com.acme.jga.rest.filters.RequestDebugFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpUtils {

    public static String dumpHttpRequest(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("Request URI=").append(request.getRequestURI());
        sb.append("\n").append("Request Method=").append(request.getMethod());
        sb.append("\n").append("Server Name=").append(request.getServerName());
        sb.append("\n").append("Remote Address=").append(request.getRemoteAddr());
        sb.append("\n").append("Headers");
        request.getHeaderNames().asIterator().forEachRemaining(name -> {
            sb.append("\n").append(name).append("=").append(request.getHeader(name));
        });
        request.getParameterNames().asIterator().forEachRemaining(name -> {
            sb.append("\n").append("param: [").append(name).append("]=[").append(request.getParameter(name)).append("]");
        });
        if (ContentCachingRequestWrapper.class.isAssignableFrom(request.getClass())) {
            Optional.ofNullable(RequestDebugFilter.DEBUG_REQ.get()).ifPresent(forceDebug -> sb.append("\n").append("Body").append(
                    ((ContentCachingRequestWrapper) request).getContentAsString()));
        }
        return sb.toString();
    }
}
