package com.igitras.blog;

import com.igitras.common.BaseApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.net.UnknownHostException;

/**
 * @author mason
 */
@SpringBootApplication
@EnableEurekaClient
@EnableResourceServer
@EnableJpaAuditing
public class Application extends BaseApplication {
    public static void main(String[] args) throws UnknownHostException {
        run(Application.class, args);
    }
}
