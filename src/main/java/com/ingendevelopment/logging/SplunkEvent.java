package com.ingendevelopment.logging;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * Created by Brian Smith on 10/27/21.
 * Description:
 */
@Data
@Builder
class SplunkEvent {
    private String host;
    private String index;
    private String source;
    private Map<String, String> event;
}
