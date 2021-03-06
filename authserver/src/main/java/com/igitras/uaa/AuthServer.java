package com.igitras.uaa;

import com.igitras.common.BaseApplication;
import com.igitras.common.prop.AppProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.net.UnknownHostException;

/**
 * @author mason
 */
@SpringBootApplication
@EnableEurekaClient
@EnableConfigurationProperties({AppProperties.class})
@EnableJpaAuditing
public class AuthServer extends BaseApplication {

    public static void main(String[] args) throws UnknownHostException {
        run(AuthServer.class, args);
    }
}
