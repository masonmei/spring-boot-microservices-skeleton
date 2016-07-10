package com.igitras.common.accesslog.undertow;

import io.undertow.server.ExchangeCompletionListener;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.accesslog.AccessLogReceiver;

/**
 * Access log completion listener.
 *
 * @author mason
 */
public class AccessLogCompletionListener implements ExchangeCompletionListener {

    private AccessLogReceiver accessLogReceiver;

    public void setAccessLogReceiver(AccessLogReceiver accessLogReceiver) {
        this.accessLogReceiver = accessLogReceiver;
    }

    @Override
    public void exchangeEvent(HttpServerExchange exchange, NextListener nextListener) {
        try {
            accessLogReceiver.logMessage("");
        } finally {
            nextListener.proceed();
        }
    }
}
