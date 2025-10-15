package org.interkambio.SistemaInventarioBackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ClockConfig {

    @Bean
    public Clock clock() {
        // Devuelve la hora actual del sistema según la zona por defecto
        return Clock.systemDefaultZone();
    }
}
