package es.uca.tfg.ceramic_affair_web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import es.uca.tfg.ceramic_affair_web.configuration.ImagenProperties;

@EnableConfigurationProperties(ImagenProperties.class)
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class CeramicAffairWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(CeramicAffairWebApplication.class, args);
	}

}
