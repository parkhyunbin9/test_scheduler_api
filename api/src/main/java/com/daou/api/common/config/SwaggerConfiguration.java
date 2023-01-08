package com.daou.api.common.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.daou.api.common.security.AuthConst;
import com.google.common.reflect.TypeResolver;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration implements WebMvcConfigurer {

	private String basePackage;
	private String groupName;
	private String title;
	private String description;

	public static String[] swaggerPattern = {
		"/api/v2/**", "/health", "/swagger-ui.html", "/swagger/**"
		, "/swagger-resources/**", "/webjars/**", "/v2/api-docs"
	};

	@Bean
	public Docket api() {
		basePackage = "com.daou.api.controller";
		groupName = "Test";
		title = "Daou Test API Server";
		description = "Hourly Users And Sales Summary API";
		TypeResolver typeResolver = new TypeResolver();
		return new Docket(DocumentationType.SWAGGER_2)
			.useDefaultResponseMessages(false)
			.alternateTypeRules(
				AlternateTypeRules.newRule(typeResolver.resolveType(Pageable.class),
					typeResolver.resolveType((MyPagable.class))))
			.groupName(groupName)
			.select()
			.apis(RequestHandlerSelectors.basePackage(basePackage))
			.build()
			.apiInfo(this.apiInfo(title, description))
			.securityContexts(Arrays.asList(securityContext()))
			.securitySchemes(Arrays.asList(apiKey())).select()
			.apis(RequestHandlerSelectors.any())
			.paths(PathSelectors.ant("/api/**"))
			.build();
	}

	private ApiInfo apiInfo(String title, String description) {
		return new ApiInfoBuilder()
			.title(title)
			.description(description)
			.build();
	}

	private ApiKey apiKey() {
		return new ApiKey("JWT", AuthConst.AUTH_HEADER, "header");
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder()
			.securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
	}

	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global",
			"accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/swagger-resources/**")
			.addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("swagger-ui.html")
			.addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**")
			.addResourceLocations("classpath:/META-INF/resources/webjars/");
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
		registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css");
		registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
		registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
	}

	@ApiModel
	static class MyPagable {

		@ApiModelProperty(value = "페이지 번호 (0...N)")
		private Integer page;

		@ApiModelProperty(value = "페이지 크기", allowableValues = "range[0,1000]")
		private Integer size;

		@ApiModelProperty(value = "정렬 (ASC|DESC)")
		private List<String> sort;

		@Builder
		public MyPagable(Integer page, Integer size, List<String> sort) {
			this.page = page;
			this.size = size;
			this.sort = sort;
		}
	}

}

