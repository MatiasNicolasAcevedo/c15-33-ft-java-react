package com.clonair.back;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    MySQLContainer<?> mysqlContainer() {
        // El my.cnf de Testcontainers 1.19.3 incluye skip-host-cache, eliminado en MySQL 8.4.
        return new MySQLContainer<>("mysql:8.4")
                .withConfigurationOverride("mysql-test-conf");
    }
}
