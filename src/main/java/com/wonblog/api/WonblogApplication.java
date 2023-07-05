package com.wonblog.api;

import com.wonblog.api.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class WonblogApplication {

	public static void main(String[] args) {
		SpringApplication.run(WonblogApplication.class, args);
	}

}
