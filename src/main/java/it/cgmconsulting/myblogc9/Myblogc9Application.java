package it.cgmconsulting.myblogc9;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(info = @Info(title = "MyBlog C9 API", version = "3.0", description = "MyBlog C9"))

public class Myblogc9Application {

	public static void main(String[] args) {
		SpringApplication.run(Myblogc9Application.class, args);
	}

}
