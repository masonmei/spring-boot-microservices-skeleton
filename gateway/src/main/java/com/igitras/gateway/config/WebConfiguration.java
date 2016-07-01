package com.igitras.gateway.config;

import com.igitras.common.prop.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author mason
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

//    @Autowired
//    private OAuth2ClientProperties oAuth2ClientProperties;

    @Autowired
    private AppProperties properties;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
    }

//    @Bean
//    public FilterRegistrationBean authClientInfoFilter() {
//        FilterRegistrationBean authFilter = new FilterRegistrationBean();
//        authFilter.setFilter(new OncePerRequestFilter() {
//            @Override
//            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//                                            FilterChain filterChain)
//                    throws ServletException, IOException {
//                String grantType = request.getParameter("grant_type");
//                String requestURI = request.getRequestURI();
//                if ("password".equals(grantType) && requestURI.contains("/oauth/token")) {
//
//                    filterChain.doFilter(new HttpServletRequestWrapper(request) {
//                        @Override
//                        public String getQueryString() {
//                            String queryString = super.getQueryString();
//                            List<String> queryParts = new ArrayList<>(3);
//                            if (hasText(queryString)) {
//                                queryParts.add(queryString);
//                            }
//                            queryParts.add(format("%s=%s", "client_id", oAuth2ClientProperties.getClientId()));
//                            if (hasText(oAuth2ClientProperties.getClientSecret())) {
//                                queryParts.add(format("%s=%s", "client_secret", oAuth2ClientProperties.getClientSecret()));
//                            }
//
//                            return collectionToDelimitedString(queryParts, "&");
//                        }
//
//                        @Override
//                        public Map<String, String[]> getParameterMap() {
//                            Map<String, String[]> params = new ParameterMap<>(super.getParameterMap());
//                            params.put("client_id", new String[]{oAuth2ClientProperties.getClientId()});
//                            if (hasText(oAuth2ClientProperties.getClientSecret())) {
//                                params.put("client_secret", new String[]{oAuth2ClientProperties.getClientId()});
//                            }
//                            return params;
//                        }
//
//                        @Override
//                        public String getParameter(String name) {
//                            String parameter = super.getParameter(name);
//                            if (name.equals("client_id")) {
//                                return oAuth2ClientProperties.getClientId();
//                            }
//                            if (name.equals("client_secret")) {
//                                return oAuth2ClientProperties.getClientSecret();
//                            }
//                            return parameter;
//                        }
//
//                        @Override
//                        public String[] getParameterValues(String name) {
//                            String[] values = super.getParameterValues(name);
//                            if (name.equals("client_id")) {
//                                return new String[]{oAuth2ClientProperties.getClientId()};
//                            }
//                            if (name.equals("client_secret")) {
//                                return new String[]{oAuth2ClientProperties.getClientSecret()};
//                            }
//                            return values;
//                        }
//
//                        @Override
//                        public Enumeration<String> getParameterNames() {
//                            Enumeration<String> enumeration = super.getParameterNames();
//
//                            ArrayList<String> names = Collections.list(enumeration);
//                            names.add("client_id");
//                            if (hasText(oAuth2ClientProperties.getClientSecret())) {
//                                names.add("client_secret");
//                            }
//                            return Collections.enumeration(names);
//                        }
//                    }, response);
//                } else {
//                    filterChain.doFilter(request, response);
//                }
//            }
//        });
//        return authFilter;
//    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = properties.getCors();
        if (config.getAllowedOrigins() != null && !config.getAllowedOrigins()
                .isEmpty()) {
            source.registerCorsConfiguration("/api/**", config);
            source.registerCorsConfiguration("/v2/api-docs/**", config);
            source.registerCorsConfiguration("/configuration/ui/**", config);
            source.registerCorsConfiguration("/management/**", config);
        }
        return new CorsFilter(source);
    }

    @Bean
    public FilterRegistrationBean corsFilterRegistrationBean(CorsFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(filter);
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        return registrationBean;
    }
}
