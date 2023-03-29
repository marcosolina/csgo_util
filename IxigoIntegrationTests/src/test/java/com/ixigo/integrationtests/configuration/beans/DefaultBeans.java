package com.ixigo.integrationtests.configuration.beans;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.ixigo.library.config.spring.IxigoStandardServiceBeans;

@ComponentScan("com.ixigo.integrationtests")
@Configuration
public class DefaultBeans extends IxigoStandardServiceBeans {

}
