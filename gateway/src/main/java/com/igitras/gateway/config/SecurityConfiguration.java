package com.igitras.gateway.config;

import static com.igitras.common.utils.Constants.Authority.ADMIN;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.Map;
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
@EnableOAuth2Sso
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final String CSRF_COOKIE_NAME = "XSRF-TOKEN";
    private static final String CSRF_HEADER_NAME = "X-XSRF-TOKEN";

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/app/**/*.{js,html}")
                .antMatchers("/dep/**")
                .antMatchers("/i18n/**")
                .antMatchers("/content/**")
                .antMatchers("/swagger-ui/index.html")
                .antMatchers("/test/**")
                .antMatchers("/h2-console/**")
                .antMatchers("/index.html", "/");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.logout()
                .and().csrf().disable().headers().frameOptions().disable()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                        .antMatchers("/api/**").authenticated()
//                        .antMatchers("/management/**").hasAuthority(ADMIN)
                        .antMatchers("/configuration/ui").permitAll();
        // @formatter:on
    }

    @Bean
    @Primary
    public OAuth2ClientContextFilter dynamicOauth2ClientContextFilter() {
        return new OAuth2ClientContextFilter() {
            private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

            @Override
            protected void redirectUser(UserRedirectRequiredException e, HttpServletRequest request,
                    HttpServletResponse response) throws IOException {
                String redirectUri = e.getRedirectUri();
                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(redirectUri);
                Map<String, String> requestParams = e.getRequestParams();
                for (Map.Entry<String, String> param : requestParams.entrySet()) {
                    builder.queryParam(param.getKey(), param.getValue());
                }

                if (e.getStateKey() != null) {
                    builder.queryParam("state", e.getStateKey());
                }

                this.redirectStrategy.sendRedirect(request, response, builder.build().encode().toUriString());
            }

            @Override
            public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
                this.redirectStrategy = redirectStrategy;
            }
        };
    }

    private static Filter csrfHeaderFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                    FilterChain filterChain) throws ServletException, IOException {
                CsrfToken csrf = (CsrfToken) request
                        .getAttribute(CsrfToken.class.getName());
                if (csrf != null) {
                    Cookie cookie = WebUtils.getCookie(request, CSRF_COOKIE_NAME);
                    String token = csrf.getToken();
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

    private static CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName(CSRF_HEADER_NAME);
        return repository;
    }
}
