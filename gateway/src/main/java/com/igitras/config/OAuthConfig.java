package com.igitras.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author mason
 */
@Configuration
@Component
public class OAuthConfig extends WebSecurityConfigurerAdapter {
    public static final String CSRF_COOKIE_NAME = "XSRF-TOKEN";
    public static final String CSRF_ANGULAR_HEADER_NAME = "X-XSRF-TOKEN";

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.antMatcher("/**").authorizeRequests()
                .antMatchers("/", "/**/*.html").permitAll()
                .anyRequest().authenticated()
                .antMatchers(HttpMethod.GET, "/api/user/**","/api/task/**").access("#oauth2.hasScope('read')")
                .antMatchers(HttpMethod.OPTIONS, "/api/user/**","/api/task/**").access("#oauth2.hasScope('read')")
                .antMatchers(HttpMethod.POST, "/api/user/**","/api/task/**").access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.PUT, "/api/user/**","/api/task/**").access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.PATCH, "/api/user/**","/api/task/**").access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.DELETE, "/api/user/**","/api/task/**").access("#oauth2.hasScope('write')")
                .and().csrf().csrfTokenRepository(this.getCsrfTokenRepository())
                .and().addFilterAfter(this.createCsrfHeaderFilter(), CsrfFilter.class);
        // @formatter:off
    }

    private CsrfTokenRepository getCsrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName(CSRF_ANGULAR_HEADER_NAME);
        return repository;
    }

    private Filter createCsrfHeaderFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
                if (csrfToken != null) {
                    Cookie cookie = WebUtils.getCookie(request, CSRF_COOKIE_NAME);
                    String token = csrfToken.getToken();
                    if (cookie == null || token != null && !token.equals(cookie.getValue())) {
                        cookie = new Cookie(CSRF_COOKIE_NAME, token);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    }
                }
                filterChain.doFilter(request, response);
            }
        };
    }
}
