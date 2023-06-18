package com.guruguruzom.gatewayservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {
    public LoggingFilter(){
        super(Config.class);
    }


    @Override
    public GatewayFilter apply(Config config) {
        //Custom pre filter

        //exchange: Webflux에선 rquest와 reponse가 존재하며 이걸 사용하게 해줌
        //chain: 다양한 필터들를 연결해줌
//        return ((exchange, chain) -> {
//            ServerHttpRequest request = exchange.getRequest();
//            ServerHttpResponse response = exchange.getResponse();
//
//            log.info("global filter baseMessage {}", config.getBaseMessage());
//
//            if(config.isPreLogger()){
//                log.info("Global Filter Start : request id -> {}", request.getId());
//            }
//            return chain.filter(exchange).then(Mono.fromRunnable(()-> {
//                //mono type : 비동기 방식에서 단일값 전송할 때 사용
//                if(config.isPostLogger()){
//                    log.info("Global Filter End : response id -> {}", response.getStatusCode());
//                }
//                //log.info("global post filter : request id -> {}", response.getStatusCode());
//            } ));
//        });
        GatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("logging filter baseMessage {}", config.getBaseMessage());

            if(config.isPreLogger()){
                log.info("logging pre Filter : request id -> {}", request.getId());
            }
            return chain.filter(exchange).then(Mono.fromRunnable(()-> {
                //mono type : 비동기 방식에서 단일값 전송할 때 사용
                if(config.isPostLogger()){
                    log.info("logging post Filter : response id -> {}", response.getStatusCode());
                }
                //log.info("global post filter : request id -> {}", response.getStatusCode());
            } ));
        }, Ordered.LOWEST_PRECEDENCE); //<-우선순위 설정

        return filter;
    }

    @Data
    public static  class Config{
        //yml file에 정보 입력
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
