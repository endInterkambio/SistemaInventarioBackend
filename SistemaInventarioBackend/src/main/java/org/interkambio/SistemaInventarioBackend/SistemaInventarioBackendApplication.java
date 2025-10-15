package org.interkambio.SistemaInventarioBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SistemaInventarioBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaInventarioBackendApplication.class, args);
	}

}
