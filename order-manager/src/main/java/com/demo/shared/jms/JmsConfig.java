package com.demo.shared.jms;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
public class JmsConfig {

    @Bean
    public MessageConverter jacksonJmsMessageConverter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter conv = new MappingJackson2MessageConverter();
        conv.setTargetType(MessageType.TEXT);
        conv.setTypeIdPropertyName("_type");
        // branche l'ObjectMapper de Spring (inclut JavaTimeModule)
        conv.setObjectMapper(objectMapper);
        return conv;
    }
}
