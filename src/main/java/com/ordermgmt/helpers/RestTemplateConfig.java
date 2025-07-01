package com.ordermgmt.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.web.client.RestTemplate;

import com.ordermgmt.models.entity.ExternalApiLogs;
import com.ordermgmt.repository.ExtApiLogsRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.stream.Collectors;

@Configuration
public class RestTemplateConfig {

	private static final Logger logger = LoggerFactory.getLogger(RestTemplateConfig.class);

	@Bean
	ClientHttpRequestInterceptor loggingInterceptor() {
		return new LoggingInterceptor();
	}

	@Bean
	RestTemplate restTemplate(ClientHttpRequestInterceptor loggingInterceptor) {

		logger.info(">>> Creating RestTemplate bean with interceptor");

		// Step 1: Base request factory with timeouts
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout(50000); // 50 seconds
		factory.setReadTimeout(60000); // 60 seconds

		// Step 2: Wrap in buffering factory
		ClientHttpRequestFactory bufferingFactory = new BufferingClientHttpRequestFactory(factory);

		// Step 3: Create RestTemplate with interceptors
		RestTemplate restTemplate = new RestTemplate(bufferingFactory);
		restTemplate.setInterceptors(Collections.singletonList(loggingInterceptor));

		return restTemplate;
	}

	public static class LoggingInterceptor implements ClientHttpRequestInterceptor {

		private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

		@Autowired
		private ExtApiLogsRepository extLogsRepo;

		@Override
		public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
				throws java.io.IOException {

			logger.info(">>> Interceptor triggered");

			ClientHttpResponse response = execution.execute(request, body);

			// Safe logging of response body
			String responseBody;
			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {

				responseBody = reader.lines().collect(Collectors.joining("\n"));
			}

			logger.info("Request URI: " + request.getURI());
			logger.info("Response Code: " + response.getStatusCode());
			logger.info("Response Body: " + responseBody);

			try {
				extLogsRepo.save(ExternalApiLogs.builder().httpMethod(request.getMethod().toString())
						.httpCode(response.getStatusCode().value()).request(new String(body, StandardCharsets.UTF_8))
						.response(responseBody).serverName(CommonFunctions.getMachineHostName())
						.url(request.getURI().toString()).httpHeaders(request.getHeaders().toString()).build());

			} catch (Exception e) {
				logger.error("Error in saving External API Logs: ", e);
			}

			return response;
		}
	}
}