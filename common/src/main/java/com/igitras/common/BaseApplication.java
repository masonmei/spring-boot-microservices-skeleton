package com.igitras.common;

import static com.igitras.common.utils.Constants.Profile.DEV;
import static com.igitras.common.utils.Constants.Profile.NATIVE;
import static com.igitras.common.utils.Constants.Profile.PROD;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import javax.annotation.PostConstruct;

/**
 * Base Application.
 *
 * @author mason
 */
public abstract class BaseApplication {
    private static final Logger LOG = LoggerFactory.getLogger(BaseApplication.class);

    @Autowired
    private Environment env;

    public static void run(Class<?> clazz, String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(clazz);
        SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);
        addDefaultProfile(app, source);
        Environment env = app.run(args)
                .getEnvironment();
        // @formatter:off
        LOG.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\thttp://127.0.0.1:{}\n\t" +
                        "External: \thttp://{}:{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"));
        // @formatter:on
    }

    @PostConstruct
    public void initApplication() throws IOException {
        if (env.getActiveProfiles().length == 0) {
            LOG.warn("No Spring profile configured, running with default configuration");
        } else {
            LOG.info("Running with Spring profile(s) : {}", Arrays.toString(env.getActiveProfiles()));
            Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
            if (activeProfiles.contains(DEV) && activeProfiles.contains(PROD)) {
                LOG.error("You have mis configured your application! "
                        + "It should not run with both the 'dev' and 'prod' profiles at the same time.");
            }
        }
    }

    /**
     * If no profile has been configured, set by default the "dev" and "native" profile.
     */
    private static void addDefaultProfile(SpringApplication app, SimpleCommandLinePropertySource source) {
        if (!source.containsProperty("spring.profiles.active") && !System.getenv()
                .containsKey("SPRING_PROFILES_ACTIVE")) {

            app.setAdditionalProfiles(DEV, NATIVE);
        }
    }
}
