package com.vaccinelife.vaccinelifeapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableJpaAuditing
//@EnableResourceServer @EnableAuthorizationServer
public class VaccinelifeApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(VaccinelifeApiApplication.class, args);
	}
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/**")
//                        .allowedOrigins("*")
//                        .maxAge(3000)
//                        .allowedHeaders("header1", "Origin", "Accept", "X-Requested-With", "Content-Type", "Access-Control-Request-Method"
//                                , "Access-Control-Request-Headers", "Authorization")
//                        .exposedHeaders("header1", "Origin", "Accept", "X-Requested-With", "Content-Type", "Access-Control-Request-Method"
//                                , "Access-Control-Request-Headers", "Authorization")
//                        .allowedMethods("PUT", "DELETE", "GET", "HEAD", "PATCH", "POST");
//			}
//		};
//	}

}
