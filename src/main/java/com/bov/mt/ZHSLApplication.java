package com.bov.mt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
public class ZHSLApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZHSLApplication.class,args);
    }

    @Bean
    MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setLocation("d:/users/temp");
		return factory.createMultipartConfig();
	}
}
