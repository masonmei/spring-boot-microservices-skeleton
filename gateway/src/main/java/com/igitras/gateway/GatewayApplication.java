package com.igitras.gateway;

import com.igitras.common.BaseApplication;
import com.igitras.common.prop.AppProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

import java.net.UnknownHostException;

/**
 * @author mason
 */
@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
@EnableConfigurationProperties({AppProperties.class})
public class GatewayApplication extends BaseApplication{

    public static void main(String[] args) throws UnknownHostException {
        run(GatewayApplication.class, args);
    }
}
