package com.ixigo.library.config.spring;

import java.lang.reflect.Field;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;

/**
 * Lets springfox 2.9.2 (Swagger) start next to Spring Boot Actuator.
 * <p>
 * Springfox sorts every {@link RequestMappingInfoHandlerMapping} by its patterns
 * condition. Since Spring Boot 2.6 the actuator endpoints are registered with a
 * PathPatternParser based mapping whose patterns condition is null, so the
 * services that enable Swagger fail at startup with a NullPointerException in
 * "documentationPluginsBootstrapper". The services' own controllers use
 * {@code spring.mvc.pathmatch.matching-strategy: ant_path_matcher} (no pattern
 * parser), so removing the mappings that have a pattern parser hides only the
 * actuator endpoints from springfox.
 * <p>
 * Remove this class when springfox is replaced (for example by springdoc).
 *
 * @author Marco
 */
@Configuration
@ConditionalOnClass(name = "springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider")
public class SpringfoxActuatorCompatibility {

	@Bean
	public static BeanPostProcessor springfoxHandlerMappingsFilter() {
		return new BeanPostProcessor() {
			@Override
			public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
				if (bean instanceof WebMvcRequestHandlerProvider) {
					handlerMappings(bean).removeIf(mapping -> mapping.getPatternParser() != null);
				}
				return bean;
			}
		};
	}

	@SuppressWarnings("unchecked")
	private static List<RequestMappingInfoHandlerMapping> handlerMappings(Object bean) {
		Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
		ReflectionUtils.makeAccessible(field);
		return (List<RequestMappingInfoHandlerMapping>) ReflectionUtils.getField(field, bean);
	}
}
