package com.ordermgmt.helpers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordermgmt.models.entity.ApiLogs;
import com.ordermgmt.repository.ApiLogsRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiLoggingFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(ApiLoggingFilter.class);

	@Autowired
	private ApiLogsRepository apiRepo;

	private final ObjectMapper objectMapper = new ObjectMapper(); // Inject if you want

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// Wrap request and response to read multiple times
		ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
		ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

		long startTime = System.currentTimeMillis();

		try {
			filterChain.doFilter(wrappedRequest, wrappedResponse);
		} finally {
			long duration = System.currentTimeMillis() - startTime;

			String requestBody = new String(wrappedRequest.getContentAsByteArray(),
					wrappedRequest.getCharacterEncoding());
			String responseBody = new String(wrappedResponse.getContentAsByteArray(),
					wrappedResponse.getCharacterEncoding());

			// Extract metadata
			String method = request.getMethod();
			String uri = request.getRequestURI();
			String query = request.getQueryString();
			String serverName = request.getServerName();
			int status = wrappedResponse.getStatus();

			if (query != null) {
				uri += "?" + query;
			}

			// Extract responseUuid safely
			String responseUuid = null;
			try {
				JsonNode jsonNode = objectMapper.readTree(responseBody);
				if (jsonNode.has("responseUuid")) {
					responseUuid = jsonNode.get("responseUuid").asText();
				}
			} catch (Exception e) {
				logger.warn("Failed to parse responseUuid from response body", e);
			}

			ApiLogs apiLogs = ApiLogs.builder().apiRequest(requestBody).apiResponse(responseBody).httpCode(status)
					.httpMethod(method).serverName(serverName).duration(duration).Url(uri).responseUuid(responseUuid).build();
			apiRepo.save(apiLogs);

			// Don't forget to copy response body back to output stream
			wrappedResponse.copyBodyToResponse();
		}
	}
}
