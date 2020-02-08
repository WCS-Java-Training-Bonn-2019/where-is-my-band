package com.wildcodeschool.sea.bonn.whereismyband.services;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// KrillMi, 08.02.2020 => needed for registering the custom built MultipartFileToByteArray converter

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new MultipartFileToByteArrayConverter());
	}
}