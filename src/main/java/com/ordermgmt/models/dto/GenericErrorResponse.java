package com.ordermgmt.models.dto;

import lombok.Data;

@Data
public class GenericErrorResponse {

	private String code = "E999999";
	private String description = "General Integration Error";
}
