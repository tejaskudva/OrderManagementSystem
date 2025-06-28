package com.ordermgmt.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;

@Configuration
public class RabbitMQConfig {

	@Bean
	Queue orderQueue() {
		return QueueBuilder.durable("order.queue").withArgument("x-dead-letter-exchange", "order.dlx")
				.withArgument("x-dead-letter-routing-key", "order.dlq").build();

	}

	@Bean
	DirectExchange deadLetterExchange() {
		return new DirectExchange("order.dlx");
	}

	@Bean
	Queue deadLetterQueue() {
		return QueueBuilder.durable("order.queue.dlq").build();
	}

	@Bean
	Binding dlqBinding() {
		return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with("order.dlq");
	}

	@Bean
	SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setPrefetchCount(10);
		factory.setConcurrentConsumers(5);
		factory.setMaxConcurrentConsumers(10);
		factory.setAcknowledgeMode(AcknowledgeMode.AUTO); // or MANUAL if you're using manual acks

		factory.setAdviceChain(RetryInterceptorBuilder.stateless()
				.maxAttempts(5)
				.backOffOptions(2000, 2.0, 20000) // initialInterval, multiplier, maxInterval
				.recoverer(new RejectAndDontRequeueRecoverer()) // sends to DLQ if configured
				.build());

		return factory;
	}

}