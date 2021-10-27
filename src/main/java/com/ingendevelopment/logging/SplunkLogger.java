package com.ingendevelopment.logging;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Brian Smith on 10/27/21.
 * Description:
 */
@Component
@Slf4j
public class SplunkLogger {

    public enum Severity {
        ERROR, WARNING, INFO
    }

    public void logMessage(String message, String invokedSource, Severity severity) {
        Unirest.config().verifySsl(false);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", MediaType.APPLICATION_JSON.toString());
        headers.put("Authorization", "Splunk 79df2c26-c7f4-4421-8b75-4a858b49361d");

        Map<String, String> eventParameters = new HashMap<>();
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

@Data
@Builder
class SplunkEvent {
    private String host;
    private String index;
    private String source;
    private Map<String, String> event;
}