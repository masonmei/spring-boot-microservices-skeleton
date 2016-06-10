package com.igitras.uaa.config;

import static com.igitras.common.utils.Constants.Authority.ADMIN;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * @author mason
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    private static final String RESOURCE_ID = "uaa";

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
         http.exceptionHandling().accessDeniedPage("/login?authorization_error=true")
                .and().csrf().ignoringAntMatchers("/api/**", "/management/**", "/oauth/**")
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/login")
                .and().formLogin().loginPage("/login").failureUrl("/login?authorization_error=true")
                .and() .authorizeRequests()
                        .antMatchers("/login/**").permitAll()
                        .antMatchers("/api/register").permitAll()
                        .antMatchers("/api/activate").permitAll()
                        .antMatchers("/api/authenticate").permitAll()
                        .antMatchers("/api/account/reset_password/init").permitAll()
                        .antMatchers("/api/account/reset_password/finish").permitAll()
//                        .antMatchers("/management/**").hasAuthority(ADMIN)
                        .antMatchers("/management/**").permitAll()
                        .antMatchers("/api/**").authenticated()
                        .antMatchers("/v2/api-docs/**").permitAll()
                        .antMatchers("/configuration/ui").permitAll();
        // @formatter:on
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(RESOURCE_ID).stateless(false);
    }
}
