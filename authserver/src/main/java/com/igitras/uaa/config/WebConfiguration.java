package com.igitras.uaa.config;

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

    @Autowired
    private AppProperties properties;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/oauth/confirm_access").setViewName("authorize");

    }

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
