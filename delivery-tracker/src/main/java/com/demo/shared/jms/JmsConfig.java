package com.demo.shared.jms;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class JmsConfig {

    @Bean
    public MessageConverter jacksonJmsMessageConverter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter conv = new MappingJackson2MessageConverter();
        conv.setTargetType(MessageType.TEXT);
        conv.setTypeIdPropertyName("_type");
        conv.setObjectMapper(objectMapper);

        // très important : mappe le type produit par order-manager vers ta classe consommateur
        Map<String, Class<?>> map = new HashMap<>();
        map.put("com.demo.ordermanager.messaging.OrderCreatedEvent",
                com.demo.deliverytracker.messaging.OrderCreatedEvent.class);
        conv.setTypeIdMappings(map);

        return conv;
    }
}
