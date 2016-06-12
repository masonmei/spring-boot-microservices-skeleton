package com.igitras.gateway.custom;

import static java.util.Collections.singletonList;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.stereotype.Component;

/**
 * @author mason
 */
@Component
public class OAuthPasswordFilter extends ZuulFilter {

    @Autowired
    private OAuth2ClientProperties properties;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 200;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String requestURI = ctx.getRequest()
                .getRequestURI();
        String[] grantTypes = ctx.getRequest()
                .getParameterValues("grant_type");
        String serviceId = (String) ctx.get("serviceId");
        return (ctx.getRouteHost() == null && serviceId != null && ctx.sendZuulResponse() && serviceId
                .equals("AUTHSERVER") && isTokenRequest(requestURI, grantTypes));
    }

    private boolean isTokenRequest(String requestUri, String... grantTypes) {
        if (!requestUri.contains("/oauth/token")) {
            return false;
        }
        for (String grantType : grantTypes) {
            if ("password".equals(grantType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.getRequestQueryParams()
                .put("client_id", singletonList(properties.getClientId()));
        ctx.getRequestQueryParams()
                .put("client_secret", singletonList(properties.getClientSecret()));
        return null;
    }
}
