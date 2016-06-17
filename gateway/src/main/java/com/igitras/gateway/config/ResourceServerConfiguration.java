package com.igitras.gateway.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @author mason
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().headers().frameOptions().disable()
                .and().sessionManagement().sessionCreationPolicy(STATELESS)
                .and().authorizeRequests()
                        .antMatchers("/api/**").authenticated()
//                        .antMatchers("/management/**").hasAuthority(ADMIN)
                        .antMatchers("/management/**").permitAll()
                        .antMatchers("/configuration/ui").permitAll();
    }
}
