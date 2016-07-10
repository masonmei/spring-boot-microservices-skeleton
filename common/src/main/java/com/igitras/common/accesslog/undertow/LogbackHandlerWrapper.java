package com.igitras.common.accesslog.undertow;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.accesslog.AccessLogReceiver;

/**
 * Logback handler wrapper for undertow.
 *
 * @author mason
 */
public class LogbackHandlerWrapper implements HttpHandler {

    private final HttpHandler next;
    private final AccessLogReceiver accessLogReceiver;
    private final AccessLogCompletionListener accessLogCompletionListener;

    public LogbackHandlerWrapper(HttpHandler next, AccessLogReceiver accessLogReceiver) {
        this.next = next;
        this.accessLogReceiver = accessLogReceiver;
        accessLogCompletionListener = new AccessLogCompletionListener();
        accessLogCompletionListener.setAccessLogReceiver(this.accessLogReceiver);
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.addExchangeCompleteListener(accessLogCompletionListener);
        next.handleRequest(exchange);
    }
}
