package com.myt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MytApplication {
static final Logger logger= LogManager.getLogger(MytApplication.class.getName());
	public static void main(String[] args) {
		logger.info("entered application");
		SpringApplication.run(MytApplication.class, args);
	}

}
