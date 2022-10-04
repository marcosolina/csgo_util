package com.ixigo.library.config.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.ixigo.library.mediators.web.implementations.WebMediatorImpl;
import com.ixigo.library.mediators.web.interfaces.WebMediator;
import com.ixigo.library.messages.IxigoMessageResource;
import com.ixigo.library.messages.IxigoReloadableResourceBundleMessageSource;

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
}
