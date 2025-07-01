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
@Table(name = "ext_api_logs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExternalApiLogs {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "TEXT")
	private String request;
	@Column(columnDefinition = "TEXT")
	private String response;

	private Integer httpCode;
	private String httpMethod;
	private String serverName;
	private String url;

	@Column(columnDefinition = "TEXT")
	private String httpHeaders;

	@CreationTimestamp
	private LocalDateTime timestamp;
}