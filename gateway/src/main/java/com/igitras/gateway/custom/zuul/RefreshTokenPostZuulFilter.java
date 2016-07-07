package com.igitras.gateway.custom.zuul;

import static com.igitras.common.utils.Constants.Cookies.REFRESH_TOKEN;
import static com.igitras.common.utils.Constants.Cookies.REFRESH_TOKEN_CACHES;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.igitras.common.prop.AppProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import javax.servlet.http.Cookie;

@Component
public class RefreshTokenPostZuulFilter extends ZuulFilter {
    private static final Logger LOG = LoggerFactory.getLogger(RefreshTokenPostZuulFilter.class);

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private AppProperties properties;

    @Override
    public Object run() {
        final RequestContext ctx = RequestContext.getCurrentContext();
        LOG.info("in zuul filter " + ctx.getRequest().getRequestURI());
        try {
            InputStream is = ctx.getResponseDataStream();
            String responseBody = IOUtils.toString(is, "UTF-8");
            if (responseBody.contains("refresh_token")) {
                Map<String, Object> responseMap = mapper.readValue(
                        responseBody, new TypeReference<Map<String, Object>>() {
                        });
                String refreshToken = responseMap.get("refresh_token").toString();
                responseMap.remove("refresh_token");
                responseBody = mapper.writeValueAsString(responseMap);

                Cookie cookie = new Cookie(REFRESH_TOKEN, refreshToken);
                cookie.setHttpOnly(true);
                // cookie.setSecure(true);
                cookie.setPath(ctx.getRequest().getContextPath() + properties.getSecurity().getClient().getAuthPath());
                cookie.setMaxAge(REFRESH_TOKEN_CACHES); // 30 days
                ctx.getResponse().addCookie(cookie);
            }
            ctx.setResponseBody(responseBody);
        } catch (IOException e) {
            LOG.error("Error occured in zuul post filter", e);
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
        return 10;
    }

    @Override
    public String filterType() {
        return "post";
    }

}
