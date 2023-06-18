package com.guruguruzom.gatewayservice.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {
    public CustomFilter(){
        super(Config.class);
    }


    @Override
    public GatewayFilter apply(Config config) {
        //Custom pre filter

        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("custom pre filter : request id -> {}", request.getId());

            return chain.filter(exchange).then(Mono.fromRunnable(()-> { 
                //mono type : 비동기 방식에서 단일값 전송할 때 사용
                log.info("custom post filter : request id -> {}", response.getStatusCode());
            } ));
        });
    }

    public static  class Config{

    }
}
