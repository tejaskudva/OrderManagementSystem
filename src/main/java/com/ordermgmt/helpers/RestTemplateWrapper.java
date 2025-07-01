package com.ordermgmt.helpers;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ordermgmt.models.configurations.ExternalAPIConfig;
import com.ordermgmt.models.configurations.ExternalApiPropertiesLoader;
import com.ordermgmt.models.dto.ExternalServiceWrapperResponse;

@Component
public class RestTemplateWrapper {

	private static final Logger logger = LoggerFactory.getLogger(RestTemplateWrapper.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ExternalApiPropertiesLoader propLoader;

	public ExternalServiceWrapperResponse externalServiceWrapper(String serviceName, String requestBody,
			String pathVariable, Map<String, String> queryParam) {

		ExternalAPIConfig apiConfig = propLoader.getApiConfig(serviceName);

		String url = apiConfig.getUrl();
		String headers = apiConfig.getHeaders();
		String method = apiConfig.getMethod();

		if (pathVariable != null && !pathVariable.isBlank()) {
			url += "/" + pathVariable;
		}

		if (queryParam != null && !queryParam.isEmpty()) {
			StringBuilder keyBuilder = new StringBuilder();
			keyBuilder.append("?");
			queryParam.forEach((key, value) -> keyBuilder.append(key).append("=").append(value).append("&"));

			keyBuilder.deleteCharAt(keyBuilder.length() - 1);

			url += keyBuilder.toString();
		}

		HttpEntity<String> entity = new HttpEntity<String>(requestBody, CommonFunctions.parseToHttpHeaders(headers));

		logger.info("Hitting the following {} API at URL: {} || with the following headers: {}", method, url, headers);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.valueOf(method), entity, String.class);

		return ExternalServiceWrapperResponse.builder().httpCode(response.getStatusCode())
				.responseBody(response.getBody()).build();

	}

}
