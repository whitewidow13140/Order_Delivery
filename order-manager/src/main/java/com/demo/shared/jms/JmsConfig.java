package com.demo.shared.jms;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import com.demo.ordermanager.messaging.OrderCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class JmsConfig {

    @Bean(name = "jacksonJmsMessageConverter")
    public MappingJackson2MessageConverter jacksonJmsMessageConverter(ObjectMapper baseOm) {
        // mapper JSON commun avec support Java Time
        ObjectMapper om = baseOm.copy().registerModule(new JavaTimeModule());

        MappingJackson2MessageConverter conv = new MappingJackson2MessageConverter();
        conv.setObjectMapper(om);
        conv.setTargetType(MessageType.TEXT);
        conv.setTypeIdPropertyName("__TypeId__");

        Map<String, Class<?>> mappings = new HashMap<>();
        mappings.put("OrderCreatedEvent", OrderCreatedEvent.class);
        conv.setTypeIdMappings(mappings);

        return conv;
    }
    // @Bean
    // public MessageConverter jacksonJmsMessageConverter(ObjectMapper objectMapper) {
    //     MappingJackson2MessageConverter conv = new MappingJackson2MessageConverter();
    //     conv.setTargetType(MessageType.TEXT);
    //     conv.setTypeIdPropertyName("_type");
    //     // branche l'ObjectMapper de Spring (inclut JavaTimeModule)
    //     conv.setObjectMapper(objectMapper);
    //     return conv;
    // }
}
