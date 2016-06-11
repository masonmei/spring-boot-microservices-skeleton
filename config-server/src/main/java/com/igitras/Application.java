package com.igitras;

import com.igitras.common.BaseApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

import java.net.UnknownHostException;

/**
 * @author mason
 */
@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class Application extends BaseApplication {

    public static void main(String[] args) throws UnknownHostException {
        run(Application.class, args);
    }
}
