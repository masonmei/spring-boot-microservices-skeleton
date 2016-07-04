package com.igitras.gateway.custom.zuul;

import static java.lang.String.format;

import com.igitras.common.prop.AppProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

@Component
public class ClientAuthPreZuulFilter extends ZuulFilter {
    private static final Logger LOG = LoggerFactory.getLogger(ClientAuthPreZuulFilter.class);

    @Autowired
    private AppProperties properties;

    @Override
    public Object run() {
        final RequestContext ctx = RequestContext.getCurrentContext();
        LOG.info("in zuul filter " + ctx.getRequest().getRequestURI());
        byte[] encoded;
        try {
            String clientAuth = format("%s:%s", properties.getSecurity().getClient().getClientId(),
                    properties.getSecurity().getClient().getClientSecret());
            encoded = Base64.encode(clientAuth.getBytes("UTF-8"));
            ctx.addZuulRequestHeader("Authorization", "Basic " + new String(encoded));
            LOG.info("pre filter");
            LOG.info(ctx.getRequest().getHeader("Authorization"));
        } catch (final UnsupportedEncodingException e) {
            LOG.error("Error occurred in pre filter", e);
        }

        return null;
    }

    @Override
    public boolean shouldFilter() {
        final RequestContext ctx = RequestContext.getCurrentContext();
        String requestURI = ctx.getRequest().getRequestURI();
        return requestURI.equals(properties.getSecurity().getClient().getAuthPath());
    }

    @Override
    public int filterOrder() {
        return -3;
    }

    @Override
    public String filterType() {
        return "pre";
    }

}
