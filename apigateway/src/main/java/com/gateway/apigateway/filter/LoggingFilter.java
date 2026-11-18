package com.gateway.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@Component
@Slf4j
public class LoggingFilter implements GlobalFilter {
   /* @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        System.out.println(
                "Incoming Request : "
                        + exchange.getRequest().getURI());

        return chain.filter(exchange);
    }*/

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {

        long startTime = System.currentTimeMillis();

        String requestUri =
                exchange.getRequest()
                        .getURI()
                        .toString();

        String method =
                exchange.getRequest()
                        .getMethod()
                        .name();

        log.info(
                "Incoming Request: {} {}",
                method,
                requestUri
        );
        return chain.filter(exchange)
                .doFinally(signalType -> {

                    long endTime =
                            System.currentTimeMillis();

                    long responseTime =
                            endTime - startTime;

                    int statusCode =
                            exchange.getResponse()
                                    .getStatusCode() != null
                                    ? exchange.getResponse()
                                    .getStatusCode()
                                    .value()
                                    : 0;

                    log.info(
                            "Completed Request: {} {} | Status: {} | Response Time: {} ms",
                            method,
                            requestUri,
                            statusCode,
                            responseTime
                    );
                });
    }

    /*
    Request Flow
    Suppose you call:GET /api/employees
Client

↓

API Gateway

↓

Logging Filter

↓

Route Matching

↓

Employee Service

↓

Response

↓

Client
explanation about filter---if any class implements GlobalFilter This tells Spring

This class is a Gateway Filter.

Spring automatically registers it.

Whenever a request comes,

Spring executes filter(...)
---new concept
ServerWebExchange

This object contains everything about the HTTP request.
final summary:
| Code                      | Purpose                                                                           |
| ------------------------- | --------------------------------------------------------------------------------- |
| `@Component`              | Registers the filter as a Spring bean.                                            |
| `implements GlobalFilter` | Executes this filter for every request that passes through the Gateway.           |
| `ServerWebExchange`       | Gives access to the incoming request and outgoing response.                       |
| `GatewayFilterChain`      | Represents the remaining filters and the route to the target service.             |
| `exchange.getRequest()`   | Reads request details such as URL, method, and headers.                           |
| `chain.filter(exchange)`  | Continues processing. Without it, the request stops at this filter.               |
| `Mono<Void>`              | Reactive return type used by Spring WebFlux to represent asynchronous completion. |


     */
}
