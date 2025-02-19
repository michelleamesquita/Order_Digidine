package com.fiap.digidine.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;

    @Value(value = "${digidine.broker.queue.order}")
    private String orderNotificationQueue;

    @Value(value = "${digidine.broker.key.orderNotificationKey}")
    private String orderNotificationKey;

    @Value(value = "${digidine.broker.queue.production}")
    private String orderProductionNotificationQueue;

    @Value(value = "${digidine.broker.key.orderProductionNotificationKey}")
    private String orderProductionNotificationKey;





    @Value("${digidine.broker.exchange.orderUpdateExchange}")
    private String orderUpdateExchangeName;

    @Value("${digidine.broker.queue.orderUpdate}")
    private String orderUpdateQueueName;

    @Value("${digidine.broker.key.orderUpdateKey}")
    private String orderUpdateKey;

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(cachingConnectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public TopicExchange orderNotificationExchange() {
        return new TopicExchange("digidine.order.notification");
    }

    // Declara a fila
    @Bean
    public Queue orderNotificationQueue() {
        return new Queue(orderNotificationQueue, true);
    }

    // Declara o binding entre a exchange e a fila com a routing key
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(orderNotificationQueue())
                .to(orderNotificationExchange())
                .with(orderNotificationKey);
    }

    @Bean
    public TopicExchange orderProductionNotificationExchange() {
        return new TopicExchange("digidine.order-production.notification");
    }

    // Declara a fila
    @Bean
    public Queue orderProductionNotificationQueue() {
        return new Queue(orderProductionNotificationQueue, true);
    }

    // Declara o binding entre a exchange e a fila com a routing key
    @Bean
    public Binding bindingOrderProduction() {
        return BindingBuilder.bind(orderNotificationQueue())
                .to(orderNotificationExchange())
                .with(orderProductionNotificationKey);
    }





//    @Bean
//    public TopicExchange orderUpdateExchange() {
//        return new TopicExchange(orderUpdateExchangeName);
//    }
//
//    @Bean
//    public Queue orderUpdateQueue() {
//        return new Queue(orderUpdateQueueName, true);
//    }
//
//    @Bean
//    public Binding orderUpdateBinding() {
//        return BindingBuilder.bind(orderUpdateQueue())
//                .to(orderUpdateExchange())
//                .with(orderUpdateKey);
//    }
}
