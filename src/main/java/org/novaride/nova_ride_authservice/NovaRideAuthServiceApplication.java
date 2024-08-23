package org.novaride.nova_ride_authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EntityScan("org.novaride.modelentity.models")
public class NovaRideAuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovaRideAuthServiceApplication.class, args);
    }

}
