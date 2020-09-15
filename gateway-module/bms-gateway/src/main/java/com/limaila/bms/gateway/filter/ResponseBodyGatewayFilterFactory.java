package com.limaila.bms.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.filter.factory.rewrite.MessageBodyDecoder;
import org.springframework.cloud.gateway.filter.factory.rewrite.MessageBodyEncoder;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
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
public class ResponseBodyGatewayFilterFactory extends AbstractGatewayFilterFactory<ResponseBodyGatewayFilterFactory.Config> {

    private final Map<String, MessageBodyDecoder> messageBodyDecoders;

    private final Map<String, MessageBodyEncoder> messageBodyEncoders;

    private final List<HttpMessageReader<?>> messageReaders;


    public ResponseBodyGatewayFilterFactory(
            List<HttpMessageReader<?>> messageReaders,
            Set<MessageBodyDecoder> messageBodyDecoders,
            Set<MessageBodyEncoder> messageBodyEncoders) {
        super(ResponseBodyGatewayFilterFactory.Config.class);
        this.messageReaders = messageReaders;
        this.messageBodyDecoders = messageBodyDecoders.stream()
                .collect(Collectors.toMap(MessageBodyDecoder::encodingType, identity()));
        this.messageBodyEncoders = messageBodyEncoders.stream()
                .collect(Collectors.toMap(MessageBodyEncoder::encodingType, identity()));
    }

    @Override
    public GatewayFilter apply(ResponseBodyGatewayFilterFactory.Config config) {
        ResponseBodyGatewayFilterFactory.ModifyResponseGatewayFilter gatewayFilter = new ResponseBodyGatewayFilterFactory.ModifyResponseGatewayFilter(config);
        gatewayFilter.setFactory(this);
        return gatewayFilter;
    }

    public static class Config {

    }

    public class ModifyResponseGatewayFilter implements GatewayFilter, Ordered {

        private final ResponseBodyGatewayFilterFactory.Config config;

        private GatewayFilterFactory<ResponseBodyGatewayFilterFactory.Config> gatewayFilterFactory;

        public ModifyResponseGatewayFilter(ResponseBodyGatewayFilterFactory.Config config) {
            this(config, null);
        }

        @Deprecated
        public ModifyResponseGatewayFilter(ResponseBodyGatewayFilterFactory.Config config,
                                           @Nullable ServerCodecConfigurer codecConfigurer) {
            this.config = config;
        }

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            return chain.filter(exchange.mutate()
                    .response(new ResponseBodyGatewayFilterFactory.ModifiedServerHttpResponse(exchange, config)).build());
        }

        @SuppressWarnings("unchecked")
        @Deprecated
        ServerHttpResponse decorate(ServerWebExchange exchange) {
            return new ResponseBodyGatewayFilterFactory.ModifiedServerHttpResponse(exchange, config);
        }

        @Override
        public int getOrder() {
            return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1;
        }

        @Override
        public String toString() {
            Object obj = (this.gatewayFilterFactory != null) ? this.gatewayFilterFactory
                    : this;
            return filterToStringCreator(obj).toString();
        }

        public void setFactory(GatewayFilterFactory<ResponseBodyGatewayFilterFactory.Config> gatewayFilterFactory) {
            this.gatewayFilterFactory = gatewayFilterFactory;
        }

    }

    protected class ModifiedServerHttpResponse extends ServerHttpResponseDecorator {

        private final ServerWebExchange exchange;

        private final ResponseBodyGatewayFilterFactory.Config config;

        public ModifiedServerHttpResponse(ServerWebExchange exchange, ResponseBodyGatewayFilterFactory.Config config) {
            super(exchange.getResponse());
            this.exchange = exchange;
            this.config = config;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {

            Class inClass = String.class;
            Class outClass = String.class;

            String originalResponseContentType = exchange
                    .getAttribute(ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);
            HttpHeaders httpHeaders = new HttpHeaders();
            // explicitly add it in this way instead of
            // 'httpHeaders.setContentType(originalResponseContentType)'
            // this will prevent exception in case of using non-standard media
            // types like "Content-Type: image"
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, originalResponseContentType);

            ClientResponse clientResponse = prepareClientResponse(body, httpHeaders);

            // TODO: flux or mono
            Mono modifiedBody = clientResponse.bodyToMono(inClass)
                    .flatMap(originalBody -> {
                        log.info(String.valueOf(originalBody));
                        return Mono.just(originalBody);
                    });

            BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody,
                    outClass);
            CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange,
                    exchange.getResponse().getHeaders());
            return bodyInserter.insert(outputMessage, new BodyInserterContext())
                    .then(Mono.defer(() -> {
                        Mono<DataBuffer> messageBody = writeBody(getDelegate(),
                                outputMessage, outClass);
                        HttpHeaders headers = getDelegate().getHeaders();
                        if (!headers.containsKey(HttpHeaders.TRANSFER_ENCODING)
                                || headers.containsKey(HttpHeaders.CONTENT_LENGTH)) {
                            messageBody = messageBody.doOnNext(data -> headers
                                    .setContentLength(data.readableByteCount()));
                        }
                        // TODO: fail if isStreamingMediaType?
                        return getDelegate().writeWith(messageBody);
                    }));
        }

        @Override
        public Mono<Void> writeAndFlushWith(
                Publisher<? extends Publisher<? extends DataBuffer>> body) {
            return writeWith(Flux.from(body).flatMapSequential(p -> p));
        }

        private ClientResponse prepareClientResponse(Publisher<? extends DataBuffer> body,
                                                     HttpHeaders httpHeaders) {
            ClientResponse.Builder builder;
            builder = ClientResponse.create(exchange.getResponse().getStatusCode(),
                    messageReaders);
            return builder.headers(headers -> headers.putAll(httpHeaders))
                    .body(Flux.from(body)).build();
        }

        private <T> Mono<T> extractBody(ServerWebExchange exchange,
                                        ClientResponse clientResponse, Class<T> inClass) {
            // if inClass is byte[] then just return body, otherwise check if
            // decoding required
            if (byte[].class.isAssignableFrom(inClass)) {
                return clientResponse.bodyToMono(inClass);
            }

            List<String> encodingHeaders = exchange.getResponse().getHeaders()
                    .getOrEmpty(HttpHeaders.CONTENT_ENCODING);
            for (String encoding : encodingHeaders) {
                MessageBodyDecoder decoder = messageBodyDecoders.get(encoding);
                if (decoder != null) {
                    return clientResponse.bodyToMono(byte[].class)
                            .publishOn(Schedulers.parallel()).map(decoder::decode)
                            .map(bytes -> exchange.getResponse().bufferFactory()
                                    .wrap(bytes))
                            .map(buffer -> prepareClientResponse(Mono.just(buffer),
                                    exchange.getResponse().getHeaders()))
                            .flatMap(response -> response.bodyToMono(inClass));
                }
            }

            return clientResponse.bodyToMono(inClass);
        }

        private Mono<DataBuffer> writeBody(ServerHttpResponse httpResponse,
                                           CachedBodyOutputMessage message, Class<?> outClass) {
            Mono<DataBuffer> response = DataBufferUtils.join(message.getBody());
            if (byte[].class.isAssignableFrom(outClass)) {
                return response;
            }

            List<String> encodingHeaders = httpResponse.getHeaders()
                    .getOrEmpty(HttpHeaders.CONTENT_ENCODING);
            for (String encoding : encodingHeaders) {
                MessageBodyEncoder encoder = messageBodyEncoders.get(encoding);
                if (encoder != null) {
                    DataBufferFactory dataBufferFactory = httpResponse.bufferFactory();
                    response = response.publishOn(Schedulers.parallel())
                            .map(encoder::encode).map(dataBufferFactory::wrap);
                    break;
                }
            }

            return response;
        }

    }

    @Deprecated
    @SuppressWarnings("unchecked")
    public class ResponseAdapter implements ClientHttpResponse {

        private final Flux<DataBuffer> flux;

        private final HttpHeaders headers;

        public ResponseAdapter(Publisher<? extends DataBuffer> body,
                               HttpHeaders headers) {
            this.headers = headers;
            if (body instanceof Flux) {
                flux = (Flux) body;
            } else {
                flux = ((Mono) body).flux();
            }
        }

        @Override
        public Flux<DataBuffer> getBody() {
            return flux;
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }

        @Override
        public HttpStatus getStatusCode() {
            return null;
        }

        @Override
        public int getRawStatusCode() {
            return 0;
        }

        @Override
        public MultiValueMap<String, ResponseCookie> getCookies() {
            return null;
        }

    }
}
