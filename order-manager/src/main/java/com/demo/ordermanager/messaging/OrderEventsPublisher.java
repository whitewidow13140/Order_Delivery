package com.demo.ordermanager.messaging;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

@Component
public class OrderEventsPublisher {

  private final JmsTemplate jmsTemplate;

  public OrderEventsPublisher(JmsTemplate jmsTemplate) {
    this.jmsTemplate = jmsTemplate;
  }

  public void publishOrderCreated(OrderCreatedEvent evt) {
    String corr = Optional.ofNullable(MDC.get("correlationId"))
                          .orElse(UUID.randomUUID().toString());
    MessagePostProcessor mpp = msg -> {
      msg.setJMSCorrelationID(corr);
      // __TypeId__ est déjà géré par MappingJackson2MessageConverter
      return msg;
    };
    jmsTemplate.convertAndSend("queue.orders.new", evt, mpp);
  }
}
