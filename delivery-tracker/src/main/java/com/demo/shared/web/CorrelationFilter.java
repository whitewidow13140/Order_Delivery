package com.demo.shared.web;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorrelationFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws ServletException, IOException {
    String corrId = Optional.ofNullable(req.getHeader("X-Correlation-Id"))
        .filter(s -> !s.isBlank())
        .orElse(UUID.randomUUID().toString());

    MDC.put("correlationId", corrId);
    MDC.put("corrId", corrId);
    res.setHeader("X-Correlation-Id", corrId);
    try {
      chain.doFilter(req, res);
    } finally {
      MDC.remove("corrId");
      MDC.remove("correlationId");
    }
  }
}
