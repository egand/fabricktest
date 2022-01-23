package com.egand.fabricktest.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "api.fabrick")
@Component
@Data
public class FabrickApiProperties {

    private String baseUrl;
    private String apiKey;
    private String authSchema;

}
