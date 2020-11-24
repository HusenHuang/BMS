package com.limaila.bms.gateway.filter;

import com.limaila.bms.common.context.RequestContext;
import com.limaila.bms.common.context.RequestContextHolder;
import com.limaila.bms.gateway.utils.WebFluxUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.ServerCodecConfigurer;
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

import java.util.List;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR;

/**
 * GatewayFilter that modifies the request body.
 */
@Slf4j
@Component
public class RequestBodyGatewayFilterFactory extends
        AbstractGatewayFilterFactory<RequestBodyGatewayFilterFactory.Config> {

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

    @Deprecated
    public RequestBodyGatewayFilterFactory(ServerCodecConfigurer codecConfigurer) {
        this(codecConfigurer.getReaders());
    }

    @Override
    @SuppressWarnings("unchecked")
    public GatewayFilter apply(Config config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange,
                                     GatewayFilterChain chain) {

                RequestContext webContext = WebFluxUtils.getWebContext(exchange);
                RequestContextHolder.setContext(webContext);
                log.info("RequestBodyGatewayFilterFactory ---- filter = " + webContext + "................");

                // 过滤GET请求
                if (exchange.getRequest().getMethod() == HttpMethod.GET) {
                    return chain.filter(exchange);
                }

                Class inClass = String.class;
                Class outClass = String.class;
                String originalResponseContentType = exchange
                        .getAttribute(ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);
                ServerRequest serverRequest = ServerRequest.create(exchange,
                        messageReaders);

                // TODO: flux or mono
                Mono<?> modifiedBody = serverRequest.bodyToMono(inClass)
                        .flatMap(originalBody -> {
                            log.info("BMS REQUEST = {}", String.valueOf(originalBody));
                            return Mono.just(originalBody);
                        })
                        .switchIfEmpty(Mono.defer(() ->
                                Mono.just(null)
                        ));
                BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody,
                        outClass);
                HttpHeaders headers = new HttpHeaders();
                headers.putAll(exchange.getRequest().getHeaders());
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

