package com.igitras.uaa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.security.KeyPair;
import javax.sql.DataSource;

/**
 * @author mason
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisConnectionFactory connectionFactory;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager).tokenStore(tokenStore())
                .accessTokenConverter(jwtAccessTokenConverter());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.passwordEncoder(passwordEncoder)
                .allowFormAuthenticationForClients();
    }

    //
    //    /**
    //     * Apply the token converter (and enhander) for token store.
    //     */
    //    @Bean
    //    public JwtTokenStore tokenStore() {
    //        return new JwtTokenStore(jwtTokenEnhancer());
    //    }
//
//    @Bean
//    public DefaultTokenServices tokenService(){
//        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
//        defaultTokenServices.setTokenStore(tokenStore());
//        defaultTokenServices.setTokenEnhancer(jwtAccessTokenConverter());
//        defaultTokenServices.setReuseRefreshToken(true);
//        return defaultTokenServices;
//    }
//
//    @Bean
    private TokenStore tokenStore() {
        return new RedisTokenStore(connectionFactory);
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
        //        converter.setSigningKey(properties.getSecurity().getAuthentication().getJwt().getSecret());
        KeyPair keyPair =
                new KeyStoreKeyFactory(new ClassPathResource("server.jks"), "igitras".toCharArray()).getKeyPair(
                        "igitras", "mdxayjy".toCharArray());
        converter.setKeyPair(keyPair);
        return converter;
    }

}
