package com.demo.shared.jms;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import com.demo.deliverytracker.messaging.OrderCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.jms.ConnectionFactory;

@Configuration
public class JmsConfig {

    @Bean
    public MappingJackson2MessageConverter jacksonJmsMessageConverter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper);
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("__TypeId__");

        Map<String, Class<?>> mappings = new HashMap<>();
        // Mappe le type envoyé par order-manager vers la classe locale
        mappings.put("com.demo.ordermanager.messaging.OrderCreatedEvent", OrderCreatedEvent.class);
        // tolérances
        mappings.put("com.demo.deliverytracker.messaging.OrderCreatedEvent", OrderCreatedEvent.class);
        mappings.put("OrderCreatedEvent", OrderCreatedEvent.class);

        converter.setTypeIdMappings(mappings);
        return converter;
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MappingJackson2MessageConverter messageConverter
    ) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory,
                                   MappingJackson2MessageConverter messageConverter) {
        JmsTemplate template = new JmsTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
