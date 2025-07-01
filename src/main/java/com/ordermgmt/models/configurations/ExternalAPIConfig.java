package com.ordermgmt.models.configurations;

import lombok.Data;

@Data
public class ExternalAPIConfig {
	private String url;
	private String method;
	private String headers;
}
