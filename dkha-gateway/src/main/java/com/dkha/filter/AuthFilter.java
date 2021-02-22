//package com.dkha.filter;
//
//import com.alibaba.fastjson.JSON;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.util.AntPathMatcher;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//
///**
// * 权限过滤器
// *
// * 
// * @since 1.0.0
// */
//@Configuration
//@ConfigurationProperties(prefix = "renren")
//public class AuthFilter implements GlobalFilter {
//    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
//    /**
//     * 不拦截的urls
//     */
//    private List<String> urls;
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//        String requestUri = request.getPath().pathWithinApplication().value();
//
//        //请求放行，无需验证权限
//        if(pathMatcher(requestUri)){
//            return chain.filter(exchange);
//        }
//
//
//        return chain.filter(exchange);
//    }
//
//    private Mono<Void> response(ServerWebExchange exchange, Object object) {
//        String json = JSON.toJSONString(object);
//        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
//        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
//        exchange.getResponse().setStatusCode(HttpStatus.OK);
//        return exchange.getResponse().writeWith(Flux.just(buffer));
//    }
//
//    private boolean pathMatcher(String requestUri){
//        for (String url : urls) {
//            if(antPathMatcher.match(url, requestUri)){
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public List<String> getUrls() {
//        return urls;
//    }
//
//    public void setUrls(List<String> urls) {
//        this.urls = urls;
//    }
//}
