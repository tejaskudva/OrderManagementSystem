package com.ordermgmt.models.dto;

import lombok.Data;

@Data
public class GenericSuccessResponse {

	private String code = "I0000000";
	private String description = "Successful Operation";

}
