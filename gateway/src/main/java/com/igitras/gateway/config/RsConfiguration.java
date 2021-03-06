package com.igitras.gateway.config;

import static com.igitras.common.utils.Constants.Authority.ADMIN;
import static com.igitras.common.utils.Constants.Authority.EDITOR;
import static com.igitras.common.utils.Constants.Authority.USER;

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

    private static final String RESOURCE_ID = "GATEWAY";
    private static final String RESOURCE_READ = RESOURCE_ID + "_READ";
    private static final String RESOURCE_WRITE = RESOURCE_ID + "_WRITE";

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.csrf().disable()
                .requestMatchers().antMatchers("/api/**", "/management/**", "/v2/api-docs/**", "/configuration/ui")
                .and().authorizeRequests()
                        .antMatchers("/v2/api-docs/**").permitAll()
                        .antMatchers("/configuration/ui").permitAll()
                        .antMatchers("/management/**").permitAll()
                        // .antMatchers("/management/**").hasAuthority(ADMIN)
                        .antMatchers(HttpMethod.GET, "/api/**").hasAuthority(USER)
                        .antMatchers(HttpMethod.POST, "/api/**").hasAnyAuthority(EDITOR, ADMIN)
                        .antMatchers(HttpMethod.PUT, "/api/**").hasAnyAuthority(EDITOR, ADMIN)
                        .antMatchers(HttpMethod.DELETE, "/api/**").hasAnyAuthority(EDITOR, ADMIN)
                        .antMatchers(HttpMethod.PATCH, "/api/**").hasAnyAuthority(EDITOR, ADMIN)
                        .antMatchers("/**").permitAll();
        // @formatter:on
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // @formatter:off
        resources.resourceId(RESOURCE_ID).tokenStore(tokenStore);
        // @formatter:on
    }
}
