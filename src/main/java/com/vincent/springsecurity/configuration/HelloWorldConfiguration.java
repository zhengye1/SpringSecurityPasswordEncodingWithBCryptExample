package com.vincent.springsecurity.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.vincent.springsecurity")
public class HelloWorldConfiguration extends WebMvcConfigurerAdapter{
	
	@Autowired
	RoleToUserProfileConverter roleToUserProfileConverter;
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry){
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		registry.viewResolver(viewResolver);
	}
	
	/*
	 * Configure ResourceHandlers to serve static resource like CSS/JS etc...
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry){
		registry.addResourceHandler("/static/**").addResourceLocations("/static/");
	}
	
	/*
	 * Configure Converter to be used
	 * In this example, we need a converter to convert string values[Roles] to UserProfiers in newUser.jsp
	 */
	@Override
	public void addFormatters(FormatterRegistry registry){
		registry.addConverter(roleToUserProfileConverter);
	}
}
