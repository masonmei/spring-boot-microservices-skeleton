package com.igitras.common.accesslog.undertow;

import ch.qos.logback.access.joran.JoranConfigurator;
import ch.qos.logback.access.spi.AccessEvent;
import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ContextBase;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.spi.AppenderAttachable;
import ch.qos.logback.core.spi.AppenderAttachableImpl;
import ch.qos.logback.core.spi.FilterAttachable;
import ch.qos.logback.core.spi.FilterAttachableImpl;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.status.ErrorStatus;
import ch.qos.logback.core.status.InfoStatus;
import ch.qos.logback.core.util.FileUtil;
import ch.qos.logback.core.util.OptionHelper;
import ch.qos.logback.core.util.StatusPrinter;
import io.undertow.server.handlers.accesslog.AccessLogReceiver;
import io.undertow.servlet.handlers.ServletRequestContext;
import io.undertow.servlet.spec.HttpServletRequestImpl;
import io.undertow.servlet.spec.HttpServletResponseImpl;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

/**
 * Logback Access log receiver for undertow.
 *
 * @author mason
 */
public class LogbackAccessLogReceiver extends ContextBase
        implements AccessLogReceiver, AppenderAttachable<IAccessEvent>, FilterAttachable<IAccessEvent> {
    public static final String DEFAULT_CONFIG_FILE = "conf" + File.separatorChar + "logback-access.xml";
    boolean started = false;
    boolean quiet = false;
    private AppenderAttachableImpl<IAccessEvent> aai = new AppenderAttachableImpl<>();
    private FilterAttachableImpl<IAccessEvent> fai = new FilterAttachableImpl<>();
    private String fileName;
    private String resource;

    @Override
    public void addAppender(Appender<IAccessEvent> newAppender) {
        aai.addAppender(newAppender);
    }

    @Override
    public Iterator<Appender<IAccessEvent>> iteratorForAppenders() {
        return aai.iteratorForAppenders();
    }

    @Override
    public Appender<IAccessEvent> getAppender(String name) {
        return aai.getAppender(name);
    }

    @Override
    public boolean isAttached(Appender<IAccessEvent> appender) {
        return aai.isAttached(appender);
    }

    @Override
    public void detachAndStopAllAppenders() {
        aai.detachAndStopAllAppenders();
    }

    @Override
    public boolean detachAppender(Appender<IAccessEvent> appender) {
        return aai.detachAppender(appender);
    }

    @Override
    public boolean detachAppender(String name) {
        return aai.detachAppender(name);
    }

    @Override
    public void addFilter(Filter<IAccessEvent> newFilter) {
        fai.addFilter(newFilter);
    }

    @Override
    public void clearAllFilters() {
        fai.clearAllFilters();
    }

    @Override
    public List<Filter<IAccessEvent>> getCopyOfAttachedFiltersList() {
        return fai.getCopyOfAttachedFiltersList();
    }

    @Override
    public FilterReply getFilterChainDecision(IAccessEvent event) {
        return fai.getFilterChainDecision(event);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public boolean isQuiet() {
        return quiet;
    }

    public void setQuiet(boolean quiet) {
        this.quiet = quiet;
    }

    @Override
    public void logMessage(String message) {
        HttpServletRequestImpl request = ServletRequestContext.current().getOriginalRequest();
        HttpServletResponseImpl response = ServletRequestContext.current().getOriginalResponse();
        UndertowServerAdapter adapter = new UndertowServerAdapter(request, response);
        IAccessEvent accessEvent = new AccessEvent(request, response, adapter);
        if (getFilterChainDecision(accessEvent) == FilterReply.DENY) {
            return;
        }
        aai.appendLoopOnAppenders(accessEvent);
    }

    /**
     * Start log receiver.
     */
    public void start() {
        configure();
        if (!isQuiet()) {
            StatusPrinter.print(getStatusManager());
        }
        started = true;
        registerShutdownHook();
    }

    /**
     * Stop log receiver when container shouting down.
     */
    public void stop() {
        aai.detachAndStopAllAppenders();
        started = false;
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    protected void configure() {
        URL configUrl = getConfigurationFileUrl();
        if (configUrl != null) {
            runJoranOnFile(configUrl);
        } else {
            addError("Could not find configuration file for logback-access");
        }
    }

    /**
     * Resolver the configuration file.
     *
     * @return the configuration file url
     */
    protected URL getConfigurationFileUrl() {
        if (fileName != null) {
            addInfo("Will use configuration file [" + fileName + "]");
            File file = new File(fileName);
            if (!file.exists()) {
                return null;
            }
            return FileUtil.fileToURL(file);
        }
        if (resource != null) {
            addInfo("Will use configuration resource [" + resource + "]");
            return this.getClass().getResource(resource);
        }

        String jettyHomeProperty = OptionHelper.getSystemProperty("undertow.home");
        String defaultConfigFile = DEFAULT_CONFIG_FILE;
        if (!OptionHelper.isEmpty(jettyHomeProperty)) {
            defaultConfigFile = jettyHomeProperty + File.separatorChar + DEFAULT_CONFIG_FILE;
        } else {
            addInfo("[undertow.home] system property not set.");
        }
        File file = new File(defaultConfigFile);
        addInfo("Assuming default configuration file [" + defaultConfigFile + "]");
        if (!file.exists()) {
            return null;
        }
        return FileUtil.fileToURL(file);
    }

    private void addError(String msg) {
        getStatusManager().add(new ErrorStatus(msg, this));
    }

    private void addInfo(String msg) {
        getStatusManager().add(new InfoStatus(msg, this));
    }

    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                stop();
            }
        }));
    }

    private void runJoranOnFile(URL configUrl) {
        try {
            JoranConfigurator jc = new JoranConfigurator();
            jc.setContext(this);
            jc.doConfigure(configUrl);
            if (getName() == null) {
                setName("LogbackRequestLog");
            }
        } catch (JoranException e) {
            // errors have been registered as status messages
        }
    }

}
