package com.igitras.common.accesslog.undertow;

import ch.qos.logback.access.spi.ServerAdapter;
import io.undertow.servlet.spec.HttpServletRequestImpl;
import io.undertow.servlet.spec.HttpServletResponseImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mason on 7/10/16.
 */
public class UndertowServerAdapter implements ServerAdapter {
    private HttpServletRequestImpl request;
    private HttpServletResponseImpl response;

    public UndertowServerAdapter(HttpServletRequestImpl request, HttpServletResponseImpl response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public long getRequestTimestamp() {
        return response.getExchange().getRequestStartTime();
    }

    @Override
    public long getContentLength() {
        return response.getContentLength();
    }

    @Override
    public int getStatusCode() {
        return response.getStatus();
    }

    @Override
    public Map<String, String> buildResponseHeaderMap() {
        Map<String, String> responseHeaderMap = new HashMap<String, String>();
        for (String key : response.getHeaderNames()) {
            String value = response.getHeader(key);
            responseHeaderMap.put(key, value);
        }
        return responseHeaderMap;
    }
}
