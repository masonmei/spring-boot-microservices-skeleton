package com.igitras.uaa.config;

import static com.igitras.common.utils.Constants.Authority.ADMIN;
import static com.igitras.common.utils.Constants.Authority.EDITOR;
import static com.igitras.common.utils.Constants.Authority.USER;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;

/**
 * @author mason
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(ResourceServerConfig.class);

    private static final String RESOURCE_ID = "UAA";

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.requestMatchers().antMatchers("/api/**", "/management/**", "/v2/api-docs/**", "/configuration/ui")
                .and().authorizeRequests()
                        .antMatchers("/api/register").permitAll()
                        .antMatchers("/api/activate").permitAll()
                        .antMatchers("/api/authenticate").permitAll()
                        .antMatchers("/api/account/reset_password/init").permitAll()
                        .antMatchers("/api/account/reset_password/finish").permitAll()
                        .antMatchers("/v2/api-docs/**").permitAll()
                        .antMatchers("/configuration/ui").permitAll()
                        .antMatchers("/management/**").permitAll()
                        // .antMatchers("/management/**").hasAuthority(ADMIN)
                        .antMatchers(HttpMethod.GET, "/api/**").hasAuthority(USER)
                        .antMatchers(HttpMethod.POST, "/api/**").hasAnyAuthority(EDITOR, ADMIN)
                        .antMatchers(HttpMethod.PUT, "/api/**").hasAnyAuthority(EDITOR, ADMIN)
                        .antMatchers(HttpMethod.DELETE, "/api/**").hasAnyAuthority(EDITOR, ADMIN)
                        .antMatchers(HttpMethod.PATCH, "/api/**").hasAnyAuthority(EDITOR, ADMIN);
        // @formatter:on
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(RESOURCE_ID)
                .stateless(false)
                .tokenStore(new JwtTokenStore(jwtAccessTokenConverter()))
        ;
    }

    protected JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        Resource resource = new ClassPathResource("igitras-public.cert");
        try {
            LOG.debug("Configure the resources server jwt access token converter.");
            String publicKey = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));
            converter.setVerifierKey(publicKey);
        } catch (IOException e) {
            LOG.warn("Reading jwt public key error.", e);
            throw new RuntimeException(e);
        }
        return converter;
    }
}
