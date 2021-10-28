package com.ingendevelopment.logging;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Brian Smith on 10/27/21.
 * Description:
 */
@Component
@Slf4j
public class SplunkLogger {

    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> eventParameters = new HashMap<>();

    public enum Severity {
        ERROR, WARNING, INFO
    }

    public void logMessage(String message, String invokedSource, Severity severity) {
        sendEvent(message, invokedSource, severity);
    }

    public void logException(Throwable exception, String invokedSource) {
        eventParameters.put("message", exception.getLocalizedMessage());
        eventParameters.put("stacktrace", Arrays.toString(exception.getStackTrace()));

        sendEvent("Uncaught Exception", invokedSource, Severity.ERROR);
    }

    public void sendEvent(String message, String invokedSource, Severity severity) {
        Unirest.config().verifySsl(false);

        headers.put("Content-Type", MediaType.APPLICATION_JSON.toString());
        headers.put("Authorization", "Splunk 79df2c26-c7f4-4421-8b75-4a858b49361d");

        eventParameters.put("event", message);
        eventParameters.put("severity", severity.name());

        SplunkEvent event = SplunkEvent.builder()
                .host("localhost")
                .index("ecommerce_app")
                .source(invokedSource)
                .event(eventParameters)
                .build();

        HttpResponse<JsonNode> response = Unirest.post("https://splunk.ingendevelopment.com:8088/services/collector/event")
                .headers(headers)
                .body(event)
                .asJson();

        log.info(String.valueOf(response.getBody()));
    }
}
