//package com.igitras.gateway.config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
//import org.springframework.util.FileCopyUtils;
//
//import java.io.IOException;
//
///**
// * @author mason
// */
//@Configuration
//public class JwtConfiguration {
//    private static final Logger LOG = LoggerFactory.getLogger(JwtConfiguration.class);
//
//    @Autowired
//    private JwtAccessTokenConverter jwtAccessTokenConverter;
//
//    @Bean
//    public TokenStore tokenStore() {
//        return new JwtTokenStore(jwtAccessTokenConverter);
//    }
//
//    @Bean
//    protected JwtAccessTokenConverter jwtTokenEnhancer() {
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        Resource resource = new ClassPathResource("igitras-public.cert");
//        try {
//            String publicKey = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));
//            converter.setVerifierKey(publicKey);
//            LOG.debug("Configure the gateway jwt access token converter");
//        } catch (IOException e) {
//            LOG.warn("Reading jwt public key error.", e);
//            throw new RuntimeException(e);
//        }
//        return converter;
//    }
//}
