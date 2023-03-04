package com.ixigo.library.config.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.ixigo.library.mediators.web.implementations.WebMediatorImpl;
import com.ixigo.library.mediators.web.interfaces.WebMediator;
import com.ixigo.library.messages.IxigoMessageResource;
import com.ixigo.library.messages.IxigoReloadableResourceBundleMessageSource;
import com.ixigo.library.rest.implementations.IxigoWebClientUtilsImpl;
import com.ixigo.library.rest.interfaces.IxigoWebClientUtils;

import reactor.netty.http.client.HttpClient;

public class IxigoStandardServiceBeans {
	@Bean
	public WebMediator getMediator(ApplicationContext ctx) {
		return new WebMediatorImpl(ctx);
	}

	@Bean
	public IxigoMessageResource getMessageSource() {
		IxigoReloadableResourceBundleMessageSource ms = new IxigoReloadableResourceBundleMessageSource();
		ms.setBasenames("classpath:/messages/errorCodes");
		return ms;
	}
	
	@Bean
    public IxigoWebClientUtils getWeWebClientUtils() {
        // @formatter:off
		final int size = 100 * 1024 * 1024;
	    final ExchangeStrategies strategies = ExchangeStrategies.builder()
	        .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
	        .build();
        WebClient.Builder builder = WebClient.builder()
        		.exchangeStrategies(strategies)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create().followRedirect(true)));
        // @formatter:on

        return new IxigoWebClientUtilsImpl(builder);
    }
}
