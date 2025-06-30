package com.ordermgmt.models.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "api_logs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiLogs {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String responseUuid;

	@Column(columnDefinition = "TEXT")
	private String apiRequest;
	@Column(columnDefinition = "TEXT")
	private String apiResponse;
	
	private Integer httpCode;
	private String httpMethod;
	private String serverName;
	private Long duration;
	private String Url;

	@CreationTimestamp
	private LocalDateTime timestamp;

}
