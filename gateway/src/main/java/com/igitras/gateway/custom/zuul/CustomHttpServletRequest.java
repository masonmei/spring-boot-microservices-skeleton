package com.igitras.gateway.custom.zuul;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Created by mason on 7/7/16.
 */
public class CustomHttpServletRequest extends HttpServletRequestWrapper {
    private final Map<String, String[]> additionalParams;
    private final HttpServletRequest request;

    public CustomHttpServletRequest(final HttpServletRequest request, final Map<String, String[]> additionalParams) {
        super(request);
        this.request = request;
        this.additionalParams = additionalParams;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        final Map<String, String[]> map = request.getParameterMap();
        final Map<String, String[]> param = new HashMap<>();
        param.putAll(map);
        param.putAll(additionalParams);
        return param;
    }
}
