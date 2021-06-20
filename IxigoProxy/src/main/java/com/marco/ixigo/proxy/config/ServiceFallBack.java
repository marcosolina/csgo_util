package com.marco.ixigo.proxy.config;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class ServiceFallBack implements FallbackProvider {

    @Override
    public String getRoute() {
        return null; // Null -> for all routes
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        return new GatewayClientResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Sorry something went wrong");
    }

}