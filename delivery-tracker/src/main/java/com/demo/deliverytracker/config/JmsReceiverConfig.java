package com.demo.deliverytracker.config;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import com.demo.deliverytracker.messaging.OrderCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.jms.ConnectionFactory;

@Configuration
public class JmsReceiverConfig {

    private static final Logger log = LoggerFactory.getLogger(JmsReceiverConfig.class);

    @Bean(name = "jacksonJmsMessageConverter")
    public MessageConverter jacksonJmsMessageConverter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper);
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type"); // on lit l'en-tête _type

        // Mappe la valeur _type -> classe locale
        converter.setTypeIdMappings(Map.of(
                "OrderCreatedEvent", OrderCreatedEvent.class
        ));

        return converter;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter
    ) {
        DefaultJmsListenerContainerFactory f = new DefaultJmsListenerContainerFactory();
        f.setConnectionFactory(connectionFactory);
        f.setMessageConverter(messageConverter);
        f.setConcurrency("1-3");
        f.setErrorHandler(t -> log.error("JMS listener error", t));
        return f;
    }
}
