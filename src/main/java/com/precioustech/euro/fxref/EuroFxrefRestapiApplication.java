package com.precioustech.euro.fxref;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableAutoConfiguration
@EnableScheduling
//@Configuration
public class EuroFxrefRestapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EuroFxrefRestapiApplication.class, args);
	}
	
//	@Bean
//	public ObjectMapper objectMapper() {
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.registerModule(new JodaModule());
//		return mapper;
//	}
	
}
