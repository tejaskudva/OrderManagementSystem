package com.ordermgmt.rabbitmq.producers;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {
    
    private final RabbitTemplate rabbitTemplate;
    private final Queue orderQueue;

    public OrderEventProducer(RabbitTemplate rabbitTemplate, Queue orderQueue) {
        this.rabbitTemplate = rabbitTemplate;
        this.orderQueue = orderQueue;
    }

    public void sendOrderCreatedEvent(Long orderId) {
        rabbitTemplate.convertAndSend(orderQueue.getName(), orderId);
    }
}