package com.ordermgmt.models.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

	private String customerName;
	private String status; // e.g., NEW, PROCESSING, COMPLETED, FAILED
	private Double totalAmount;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}
