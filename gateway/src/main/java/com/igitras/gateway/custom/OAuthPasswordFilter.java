//package com.igitras.gateway.custom;
//
//import static org.springframework.util.StringUtils.collectionToDelimitedString;
//import static org.springframework.util.StringUtils.hasText;
//
//import static java.lang.String.format;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
//import org.springframework.boot.context.embedded.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Enumeration;
//import java.util.List;
//import java.util.Map;
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletRequestWrapper;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * @author mason
// */
//@Component
//public class OAuthPasswordFilter implements Filter {
//
//    @Autowired
//    private OAuth2ClientProperties properties;
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//
//
////
////
////
////    @Autowired
////    public OAuthPasswordFilter(ProxyRequestHelper helper, RibbonCommandFactory<?> ribbonCommandFactory) {
////        super(helper, ribbonCommandFactory);
////    }
////
////    public OAuthPasswordFilter(RibbonCommandFactory<?> ribbonCommandFactory) {
////        super(ribbonCommandFactory);
////    }
////
////    @Override
////    public int filterOrder() {
////        return super.filterOrder() - 1;
////    }
////
////    @Override
////    public boolean shouldFilter() {
////        RequestContext ctx = RequestContext.getCurrentContext();
////        String requestURI = ctx.getRequest()
////                .getRequestURI();
////        List<String> grantTypes = Arrays.asList(ctx.getRequest().getParameterValues("grant_type"));
////        String serviceId = (String) ctx.get("serviceId");
////        return (super.shouldFilter() && serviceId.equals("AUTHSERVER") && isTokenRequest(requestURI, grantTypes));
////    }
////
////    private boolean isTokenRequest(String requestUri, List<String> grantTypes) {
////        if (!requestUri.contains("/oauth/token") || isEmpty(grantTypes)) {
////            return false;
////        }
////
////        for (String grantType : grantTypes) {
////            if ("password".equals(grantType)) {
////                return true;
////            }
////        }
////        return false;
////    }
////
////    @Override
////    public Object run() {
////        return super.run();
////    }
////
////    @Override
////    protected RibbonCommandContext buildCommandContext(RequestContext context) {
////        RibbonCommandContext txt = super.buildCommandContext(context);
////
////        MultiValueMap<String, String> params = txt.getParams();
////        List<String> grantType = params.get("grant_type");
////        String serviceId = txt.getServiceId();
////        String uri = txt.getUri();
////        if ("AUTHSERVER".equalsIgnoreCase(serviceId) && isTokenRequest(uri, grantType)) {
////            params.put("client_id", singletonList(properties.getClientId()));
////            params.put("client_secret", singletonList(properties.getClientSecret()));
////        }
////
////        return new RibbonCommandContext(serviceId, txt.getVerb(), uri,
////                txt.getRetryable(), txt.getHeaders(), params,
////                txt.getRequestEntity());
////    }
//}
