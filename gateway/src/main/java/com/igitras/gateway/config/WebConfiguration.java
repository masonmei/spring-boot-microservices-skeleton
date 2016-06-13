package com.igitras.gateway.config;

import static org.springframework.util.StringUtils.collectionToDelimitedString;
import static org.springframework.util.StringUtils.hasText;

import static java.lang.String.format;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

/**
 * @author mason
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private OAuth2ClientProperties properties;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
    }

    @Bean
    public FilterRegistrationBean authClientInfoFilter() {
        FilterRegistrationBean authFilter = new FilterRegistrationBean();
        authFilter.setFilter(new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain)
                    throws ServletException, IOException {
                String grantType = request.getParameter("grant_type");
                String requestURI = request.getRequestURI();
                if ("password".equals(grantType) && requestURI.contains("/oauth/token")) {

                    filterChain.doFilter(new HttpServletRequestWrapper(request) {
                        @Override
                        public String getQueryString() {
                            String queryString = super.getQueryString();
                            List<String> queryParts = new ArrayList<>(3);
                            if (hasText(queryString)) {
                                queryParts.add(queryString);
                            }
                            queryParts.add(format("%s=%s", "client_id", properties.getClientId()));
                            if (hasText(properties.getClientSecret())) {
                                queryParts.add(format("%s=%s", "client_secret", properties.getClientSecret()));
                            }

                            return collectionToDelimitedString(queryParts, "&");
                        }

                        @Override
                        public Map<String, String[]> getParameterMap() {
                            Map<String, String[]> params = super.getParameterMap();
                            params.put("client_id", new String[]{properties.getClientId()});
                            if (hasText(properties.getClientSecret())) {
                                params.put("client_secret", new String[]{properties.getClientId()});
                            }
                            return params;
                        }

                        @Override
                        public String getParameter(String name) {
                            String parameter = super.getParameter(name);
                            if (name.equals("client_id")) {
                                return properties.getClientId();
                            }
                            if (name.equals("client_secret")) {
                                return properties.getClientSecret();
                            }
                            return parameter;
                        }

                        @Override
                        public String[] getParameterValues(String name) {
                            String[] values = super.getParameterValues(name);
                            if (name.equals("client_id")) {
                                return new String[]{properties.getClientId()};
                            }
                            if (name.equals("client_secret")) {
                                return new String[]{properties.getClientSecret()};
                            }
                            return values;
                        }

                        @Override
                        public Enumeration<String> getParameterNames() {
                            Enumeration<String> enumeration = super.getParameterNames();

                            ArrayList<String> names = Collections.list(enumeration);
                            names.add("client_id");
                            if (hasText(properties.getClientSecret())) {
                                names.add("client_secret");
                            }
                            return Collections.enumeration(names);
                        }
                    }, response);
                } else {
                    filterChain.doFilter(request, response);
                }
            }
        });
        return authFilter;
    }
}
