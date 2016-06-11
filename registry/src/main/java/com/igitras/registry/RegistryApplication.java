package com.igitras.registry;

import com.igitras.common.BaseApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import java.net.UnknownHostException;

/**
 * @author mason
 */
@SpringBootApplication
@EnableEurekaServer
@EnableEurekaClient
public class RegistryApplication extends BaseApplication {

    public static void main(String[] args) throws UnknownHostException {
        run(RegistryApplication.class, args);
    }
}
