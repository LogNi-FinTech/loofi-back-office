package com.loofi.backoffice.webconfing;

import org.springframework.context.annotation.Configuration;

@Configuration
public class CorsConfig {

	// @Value("${allowed.origin}")
	private String allowedOrigin = "http://localhost:4200/";

//	@Bean
//	public WebMvcConfigurer getCorsConfiguration() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "PUT", "DELETE")
//						.allowedHeaders("*");
//			}
//		};
//	}
}
