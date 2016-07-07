package com.igitras.gateway.custom.zuul;

import static com.igitras.common.utils.Constants.Cookies.REFRESH_TOKEN;

import com.igitras.common.prop.AppProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by mason on 7/7/16.
 */
@Component
public class RefreshTokenPreZuulFilter extends ZuulFilter {
    private static final Logger LOG = LoggerFactory.getLogger(RefreshTokenPreZuulFilter.class);

    @Autowired
    private AppProperties properties;

    @Override
    public Object run() {
        final RequestContext ctx = RequestContext.getCurrentContext();
        LOG.info("in zuul filter " + ctx.getRequest().getRequestURI());
        LOG.info("refresh token pre filter");

        final HttpServletRequest req = ctx.getRequest();
        String grantType = req.getParameter("grant_type");
        if (!"refresh_token".equals(grantType)) {
            LOG.info("not a refresh token request, ignored.");
            return null;
        }

        final String refreshToken = extractRefreshToken(req);
        if (refreshToken != null) {
            final Map<String, String[]> param = new HashMap<>();
            param.put("refresh_token", new String[]{refreshToken});

            ctx.setRequest(new CustomHttpServletRequest(req, param));
        }

        return null;
    }

    private String extractRefreshToken(HttpServletRequest req) {
        final Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase(REFRESH_TOKEN)) {
                    return cookie.getValue();
                }
            }
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
        return -2;
    }

    @Override
    public String filterType() {
        return "pre";
    }
}
