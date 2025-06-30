package com.ordermgmt.models.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenericApiResponse {

	private String responseUuid;
	private String description;

	public GenericApiResponse(String description) {
		this.responseUuid = UUID.randomUUID().toString();
		this.description = description;
	}
	
	public GenericApiResponse() {
		this.responseUuid = UUID.randomUUID().toString();
	}

}
