package com.jomardev25.touristservice.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {
	    return new Docket(DocumentationType.SWAGGER_2)
	      .apiInfo(apiInfo())
	      .select()
	      .apis(RequestHandlerSelectors.any())
	      .paths(PathSelectors.any())
	      .build();
	}

	private ApiInfo apiInfo() {
	    return new ApiInfo(
	      "Tourist Service REST API",
	      "TrustBridge Global Coding Assestment",
	      "1.0",
	      null,
	      new Contact(null, null, null),
	      null,
	      null,
	      Collections.emptyList());
	}

}
