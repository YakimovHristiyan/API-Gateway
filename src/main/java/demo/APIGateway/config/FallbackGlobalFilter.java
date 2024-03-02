package demo.APIGateway.config;

import demo.APIGateway.constant.FilterOrderingConstants;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.ConnectException;

@Component
@Order(FilterOrderingConstants.FALLBACK_FILTER_ORDER)
public class FallbackGlobalFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain
                .filter(exchange)
                .onErrorResume(e -> {
                    if (e instanceof ConnectException) {
                        exchange
                                .getResponse()
                                .setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
                    } else {
                        exchange
                                .getResponse()
                                .setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    return exchange
                            .getResponse()
                            .setComplete();
                });
    }
}