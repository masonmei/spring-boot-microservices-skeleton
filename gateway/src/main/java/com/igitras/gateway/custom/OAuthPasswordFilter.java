package com.igitras.gateway.custom;

import static org.springframework.util.CollectionUtils.isEmpty;

import static java.util.Collections.singletonList;

import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandContext;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandFactory;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonRoutingFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.List;

/**
 * @author mason
 */
@Component
public class OAuthPasswordFilter extends RibbonRoutingFilter {

    @Autowired
    private OAuth2ClientProperties properties;



    @Autowired
    public OAuthPasswordFilter(ProxyRequestHelper helper, RibbonCommandFactory<?> ribbonCommandFactory) {
        super(helper, ribbonCommandFactory);
    }

    public OAuthPasswordFilter(RibbonCommandFactory<?> ribbonCommandFactory) {
        super(ribbonCommandFactory);
    }

    @Override
    public int filterOrder() {
        return super.filterOrder() - 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String requestURI = ctx.getRequest()
                .getRequestURI();
        List<String> grantTypes = Arrays.asList(ctx.getRequest().getParameterValues("grant_type"));
        String serviceId = (String) ctx.get("serviceId");
        return (super.shouldFilter() && serviceId.equals("AUTHSERVER") && isTokenRequest(requestURI, grantTypes));
    }

    private boolean isTokenRequest(String requestUri, List<String> grantTypes) {
        if (!requestUri.contains("/oauth/token") || isEmpty(grantTypes)) {
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
        return super.run();
    }

    @Override
    protected RibbonCommandContext buildCommandContext(RequestContext context) {
        RibbonCommandContext txt = super.buildCommandContext(context);

        MultiValueMap<String, String> params = txt.getParams();
        List<String> grantType = params.get("grant_type");
        String serviceId = txt.getServiceId();
        String uri = txt.getUri();
        if ("AUTHSERVER".equalsIgnoreCase(serviceId) && isTokenRequest(uri, grantType)) {
            params.put("client_id", singletonList(properties.getClientId()));
            params.put("client_secret", singletonList(properties.getClientSecret()));
        }

        return new RibbonCommandContext(serviceId, txt.getVerb(), uri,
                txt.getRetryable(), txt.getHeaders(), params,
                txt.getRequestEntity());
    }
}
