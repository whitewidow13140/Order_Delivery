package com.demo.shared.web;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class CorrelationFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws ServletException, IOException {
    String corrId = Optional.ofNullable(req.getHeader("X-Correlation-Id"))
        .filter(s -> !s.isBlank())
        .orElse(UUID.randomUUID().toString());

    MDC.put("corrId", corrId);
    res.setHeader("X-Correlation-Id", corrId);
    try {
      chain.doFilter(req, res);
    } finally {
      MDC.remove("corrId");
    }
  }
}
