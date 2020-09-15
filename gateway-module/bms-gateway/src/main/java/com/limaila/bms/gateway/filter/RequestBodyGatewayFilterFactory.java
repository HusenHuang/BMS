package com.limaila.bms.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.limaila.bms.common.constants.HeaderConstant;
import com.limaila.bms.common.context.RequestContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.util.List;

import static org.springframework.cloud.gateway.support.GatewayToStringStyler.filterToStringCreator;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR;

/***
 说明: 
 @author MrHuang
 @date 2020/9/14 11:20
 @desc
 ***/
@Slf4j
@Component
public class RequestBodyGatewayFilterFactory extends AbstractGatewayFilterFactory<RequestBodyGatewayFilterFactory.Config> {

    private final List<HttpMessageReader<?>> messageReaders;

    public RequestBodyGatewayFilterFactory() {
        super(Config.class);
        this.messageReaders = HandlerStrategies.withDefaults().messageReaders();
    }

    public RequestBodyGatewayFilterFactory(
            List<HttpMessageReader<?>> messageReaders) {
        super(Config.class);
        this.messageReaders = messageReaders;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange,
                                     GatewayFilterChain chain) {

                ServerRequest serverRequest = ServerRequest.create(exchange, messageReaders);

                Class inClass = String.class;
                Class outClass = String.class;
                String originalResponseContentType = exchange.getAttribute(ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);

                // TODO: flux or mono
                Mono<?> modifiedBody = serverRequest.bodyToMono(inClass)
                        .flatMap(originalBody -> {
                            log.info(String.valueOf(originalBody));
                            return Mono.just(originalBody);
                        }).switchIfEmpty(Mono.defer(() -> Mono.just(null)));

                BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, outClass);
                HttpHeaders headers = new HttpHeaders();

                headers.putAll(exchange.getRequest().getHeaders());


                try {
                    // 设置上下文到Header中
                    headers.add(HeaderConstant.HEADER_CONTEXT, URLEncoder.encode(JSON.toJSONString(RequestContextHolder.getContext()), "UTF-8"));
                } catch (Throwable e) {
                    e.printStackTrace();
                }

                // the new content type will be computed by bodyInserter
                // and then set in the request decorator
                headers.remove(HttpHeaders.CONTENT_LENGTH);

                // if the body is changing content types, set it here, to the bodyInserter
                // will know about it
                if (originalResponseContentType != null) {
                    headers.set(HttpHeaders.CONTENT_TYPE, originalResponseContentType);
                }
                CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(
                        exchange, headers);
                return bodyInserter.insert(outputMessage, new BodyInserterContext())
                        // .log("modify_request", Level.INFO)
                        .then(Mono.defer(() -> {
                            ServerHttpRequest decorator = decorate(exchange, headers,
                                    outputMessage);
                            return chain
                                    .filter(exchange.mutate().request(decorator).build());
                        }));
            }

            @Override
            public String toString() {
                return filterToStringCreator(RequestBodyGatewayFilterFactory.this).toString();
            }
        };
    }

    ServerHttpRequestDecorator decorate(ServerWebExchange exchange, HttpHeaders headers,
                                        CachedBodyOutputMessage outputMessage) {
        return new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public HttpHeaders getHeaders() {
                long contentLength = headers.getContentLength();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(headers);
                if (contentLength > 0) {
                    httpHeaders.setContentLength(contentLength);
                } else {
                    // TODO: this causes a 'HTTP/1.1 411 Length Required' // on
                    // httpbin.org
                    httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                }
                return httpHeaders;
            }

            @Override
            public Flux<DataBuffer> getBody() {
                return outputMessage.getBody();
            }
        };
    }

    public static class Config {

    }


}
