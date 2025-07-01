package com.ordermgmt.models.configurations;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "extapiconfig")
@PropertySource("file:${user.dir}/webservice-config.properties")
public class ExternalApiPropertiesLoader {

	private Map<String, ExternalAPIConfig> apis = new HashMap<>();

	public ExternalAPIConfig getApiConfig(String apiName) {
		return apis.get(apiName);
	}

}
