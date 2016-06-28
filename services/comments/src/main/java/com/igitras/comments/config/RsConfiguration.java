package com.igitras.comments.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * Resource server configuration.
 *
 * @author mason
 */
@Configuration
@EnableResourceServer
public class RsConfiguration extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "COMMENT";
    private static final String RESOURCE_READ = RESOURCE_ID + "_READ";
    private static final String RESOURCE_WRITE = RESOURCE_ID + "_WRITE";

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.csrf().disable()
                .authorizeRequests().antMatchers("/**").authenticated()
                .antMatchers(HttpMethod.GET, "/api/**").hasAuthority(RESOURCE_READ)
                .antMatchers(HttpMethod.POST, "/api/**").hasAuthority(RESOURCE_WRITE)
                .antMatchers(HttpMethod.PUT, "/api/**").hasAuthority(RESOURCE_WRITE)
                .antMatchers(HttpMethod.DELETE, "/api/**").hasAuthority(RESOURCE_WRITE)
                .antMatchers(HttpMethod.PATCH, "/api/**").hasAuthority(RESOURCE_WRITE);
        // @formatter:on
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // @formatter:off
        resources.resourceId(RESOURCE_ID).tokenStore(tokenStore);
        // @formatter:on
    }
}
