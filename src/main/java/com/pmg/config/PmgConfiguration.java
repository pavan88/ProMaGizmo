package com.pmg.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.pmg")
@PropertySource("classpath:pmg.properties")
@Import(value = { PmgSecurityConfig.class })
public class PmgConfiguration extends WebMvcConfigurerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(PmgConfiguration.class);

	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
		internalResourceViewResolver.setViewClass(JstlView.class);
		internalResourceViewResolver.setPrefix("/WEB-INF/view/");
		internalResourceViewResolver.setSuffix(".jsp");

		return internalResourceViewResolver;

	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		logger.info("*************************");
		logger.info("Property @Value Resolver");
		logger.info("*************************");
		return new PropertySourcesPlaceholderConfigurer();
	}

	/*
	 * Configure MessageSource to provide internationalized messages
	 *
	 */

	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource resource = new ReloadableResourceBundleMessageSource();
		resource.setDefaultEncoding("UTF-8");
		resource.setUseCodeAsDefaultMessage(true);
		// resource.setBasename("classpath:pmg");
		// resource.setBasename("file:///D:\\git\\properties\\config\\pmg");
		resource.setCacheSeconds(10);
		logger.info("********************************");
		logger.info("Returning the Message Resource:" + resource.toString());
		logger.info("********************************");
		// logger.info("Value:::::" + resource.getMessage("name",
		// null,Locale.getDefault()));
		return resource;
	}

	/*
	 * Configure ResourceHandlers to serve static resources like CSS/ Javascript
	 * etc...
	 *
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("/static/");
	}

}
