package com.alphaomega.alphaomegarestfulapi;

import com.alphaomega.alphaomegarestfulapi.security.util.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class AlphaOmegaRestfulApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlphaOmegaRestfulApiApplication.class, args);
	}

}
