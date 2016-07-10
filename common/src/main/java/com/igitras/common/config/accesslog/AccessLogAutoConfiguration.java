package com.igitras.common.config.accesslog;

import static com.igitras.common.utils.Constants.Property.ACCESS_LOG_ENABLED;
import static com.igitras.common.utils.Constants.Property.SWAGGER_ENABLED;

import ch.qos.logback.access.servlet.TeeFilter;
import ch.qos.logback.access.tomcat.LogbackValve;
import com.igitras.common.accesslog.jetty.JettyAccessLogCustomizer;
import com.igitras.common.accesslog.undertow.LogbackAccessLogReceiver;
import com.igitras.common.accesslog.undertow.LogbackHandlerWrapper;
import com.igitras.common.prop.AppProperties;
import com.igitras.common.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.undertow.UndertowDeploymentInfoCustomizer;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * Created by mason on 7/10/16.
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(ACCESS_LOG_ENABLED)
public class AccessLogAutoConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(AccessLogAutoConfiguration.class);

    @Autowired
    private AppProperties properties;

    /**
     * Custom the container with access log enabled.
     *
     * @return container customizer
     */
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        LOG.info("enable access log.");
        final File file = FileUtils.resolveFile(properties.getAccessLog().getConfigFile());

        return container -> {
            if (container instanceof TomcatEmbeddedServletContainerFactory) {
                TomcatEmbeddedServletContainerFactory factory = (TomcatEmbeddedServletContainerFactory) container;
                LogbackValve requestLog = new LogbackValve();
                requestLog.setFilename(file.getAbsolutePath());
                factory.getValves()
                        .add(requestLog);
            }

            if (container instanceof JettyEmbeddedServletContainerFactory) {
                JettyEmbeddedServletContainerFactory factory = (JettyEmbeddedServletContainerFactory) container;
                factory.addServerCustomizers(new JettyAccessLogCustomizer(file));
            }

            if (container instanceof UndertowEmbeddedServletContainerFactory) {
                UndertowEmbeddedServletContainerFactory factory = (UndertowEmbeddedServletContainerFactory) container;
                factory.addDeploymentInfoCustomizers(
                        (UndertowDeploymentInfoCustomizer) deploymentInfo -> deploymentInfo.addInitialHandlerChainWrapper(
                                handler -> {
                                    LogbackAccessLogReceiver accessLogReceiver = new LogbackAccessLogReceiver();
                                    accessLogReceiver.setFileName(file.getAbsolutePath());
                                    if (!accessLogReceiver.isStarted()) {
                                        accessLogReceiver.start();
                                    }
                                    return new LogbackHandlerWrapper(handler, accessLogReceiver);
                                }));
            }
        };
    }

    /**
     * Tee filter.
     *
     * @return tee filter
     */
    @Bean
    public FilterRegistrationBean teeFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new TeeFilter());
        return registrationBean;
    }
}
