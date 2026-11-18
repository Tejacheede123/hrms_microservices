package com.gateway.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {
    /*  @Bean
      public SecurityWebFilterChain securityWebFilterChain(
              ServerHttpSecurity http) {

          return http
                  .csrf(ServerHttpSecurity.CsrfSpec::disable)

                  .authorizeExchange(exchange -> exchange

                          .pathMatchers(
                                  "/actuator/**"
                          ).permitAll()

                          .pathMatchers(
                                  "/api/auth/**"
                          ).permitAll()

                          .anyExchange()
                          .authenticated()
                  )
                  .oauth2ResourceServer(oauth2 ->
                          oauth2.jwt(jwt -> {})
                  )

                  .build();
      }*/
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/actuator/**").permitAll()
                        .pathMatchers("/api/auth/**").permitAll()

                        .pathMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        .anyExchange()
                        .permitAll()
                )

                .build();
    }
}
