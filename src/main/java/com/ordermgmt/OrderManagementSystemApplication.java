package com.ordermgmt;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class OrderManagementSystemApplication {

	@Value("${order.batchsize}")
	private int batchSize;

	public static void main(String[] args) {
		SpringApplication.run(OrderManagementSystemApplication.class, args);
	}

	@Bean(name = "orderTaskExecutor")
	TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(batchSize);
		executor.setThreadNamePrefix("OrderExecutor-");
		executor.initialize();
		return executor;
	}

	@Bean
	RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.connectTimeout(Duration.ofSeconds(50)).readTimeout(Duration.ofSeconds(60)).build();
	}

}
