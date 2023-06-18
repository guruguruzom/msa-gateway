package com.guruguruzom.gatewayservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {
    public GlobalFilter(){
        super(Config.class);
    }


    @Override
    public GatewayFilter apply(Config config) {
        //Custom pre filter

        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("global filter baseMessage {}", config.getBaseMessage());

            if(config.isPreLogger()){
                log.info("Global Filter Start : request id -> {}", request.getId());
            }
            return chain.filter(exchange).then(Mono.fromRunnable(()-> { 
                //mono type : 비동기 방식에서 단일값 전송할 때 사용
                if(config.isPostLogger()){
                    log.info("Global Filter End : response id -> {}", response.getStatusCode());
                }
                //log.info("global post filter : request id -> {}", response.getStatusCode());
            } ));
        });
    }

    @Data
    public static  class Config{
        //yml file에 정보 입력
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;

    }
}
