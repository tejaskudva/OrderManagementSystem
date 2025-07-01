package com.ordermgmt.models.dto;

import org.springframework.http.HttpStatusCode;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExternalServiceWrapperResponse {

	private HttpStatusCode httpCode;
	private String responseBody;

}
