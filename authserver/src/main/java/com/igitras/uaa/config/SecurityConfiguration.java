package com.igitras.uaa.config;

import static org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED;

import com.igitras.uaa.custom.security.RedisCsrfTokenRepository;
import com.igitras.uaa.custom.security.UaaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.ManagementServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;

import java.security.KeyPair;
import javax.sql.DataSource;

/**
 * @author mason
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true,
        proxyTargetClass = true)
@Order(ManagementServerProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    public static final String XSRF_TOKEN = "XSRF-TOKEN";
    public static final String X_XSRF_TOKEN = "X-XSRF-TOKEN";

    @Autowired
    private UaaUserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RedisConnectionFactory connectionFactory;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/app/**/*.{js,html}")
                .antMatchers("/bower_components/**")
                .antMatchers("/i18n/**")
                .antMatchers("/content/**")
                .antMatchers("/swagger-ui/index.html")
                .antMatchers("/test/**")
                .antMatchers("/h2-console/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.sessionManagement().sessionCreationPolicy(IF_REQUIRED);

        http.exceptionHandling().accessDeniedPage("/login?authorization_error=true")
                .and().csrf().csrfTokenRepository(csrfTokenRepository())
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/login")
                .and().formLogin().failureUrl("/login?authorization_error=true").loginPage("/login")
                .and().rememberMe().tokenRepository(persistentTokenRepository());
        // @formatter:on
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepositoryImpl = new JdbcTokenRepositoryImpl();
        tokenRepositoryImpl.setDataSource(dataSource);
        return tokenRepositoryImpl;
    }

    private CsrfTokenRepository csrfTokenRepository() {
        RedisTemplate<String, CsrfToken> redisTemplate = redisTemplate();
        RedisCsrfTokenRepository repository = new RedisCsrfTokenRepository(redisTemplate);
        repository.setHeaderName(X_XSRF_TOKEN);
        return repository;
    }

    private RedisTemplate<String, CsrfToken> redisTemplate() {
        RedisTemplate<String, CsrfToken> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * This bean generates an token enhancer, which manages the exchange between JWT acces tokens and Authentication
     * in both direction.
     *
     * @return an access token converter configured with JHipsters secret key
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        KeyPair keyPair =
                new KeyStoreKeyFactory(new ClassPathResource("server.jks"), "igitras".toCharArray()).getKeyPair(
                        "igitras", "mdxayjy".toCharArray());
        converter.setKeyPair(keyPair);
        return converter;
    }

}
