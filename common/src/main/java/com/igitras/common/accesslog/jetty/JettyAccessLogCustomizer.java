package com.igitras.common.accesslog.jetty;

import ch.qos.logback.access.jetty.RequestLogImpl;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.util.Assert;

import java.io.File;

/**
 * Jetty Access log customizer.
 *
 * @author mason
 */
public class JettyAccessLogCustomizer implements JettyServerCustomizer {

    private final File configFile;

    /**
     * Construct with a non empty config file.
     *
     * @param configFile config file
     */
    public JettyAccessLogCustomizer(File configFile) {
        Assert.notNull(configFile, "Configuration file must not be null while creating jetty access log customizer");
        this.configFile = configFile;
    }

    @Override
    public void customize(Server server) {
        RequestLogImpl requestLog = new RequestLogImpl();
        requestLog.setFileName(configFile.getAbsolutePath());
        RequestLogHandler requestLogHandler = new RequestLogHandler();
        requestLogHandler.setRequestLog(requestLog);
        requestLogHandler.setHandler(server.getHandler());
        server.setHandler(requestLogHandler);
    }
}
