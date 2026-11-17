package com.example.dynamicAccess.DynamicDatabaseAccess.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DatabaseRefreshConfig {

    private final ConfigurableEnvironment environment;
    private final ContextRefresher contextRefresher;
    private final ApplicationEventPublisher eventPublisher; // <-- Nodig om de wijziging door te geven

    @Bean
    @Primary
    @RefreshScope
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @RefreshScope
    public DataSource dataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    /**
     * Dynamically updates the database credentials in memory, notifies the Spring
     * Environment of the change, and triggers a context refresh to restart
     * the connection pool with the new username and password.
     *
     * @param newUsername the new database username
     * @param newPassword the new database password
     */
    public void updateDatabaseCredentials(String newUsername, String newPassword) {
        Map<String, Object> dynamicProps = new HashMap<>();
        dynamicProps.put("spring.datasource.username", newUsername);
        dynamicProps.put("spring.datasource.password", newPassword);

        MutablePropertySources sources = environment.getPropertySources();
        if (sources.contains("dynamic-db-source")) {
            // Update de bestaande property source
            MapPropertySource ps = (MapPropertySource) sources.get("dynamic-db-source");
            ps.getSource().putAll(dynamicProps);
        } else {
            sources.addFirst(new MapPropertySource("dynamic-db-source", dynamicProps));
        }

        // 1. Publiceer een EnvironmentChangeEvent zodat Spring Cloud weet dat deze keys gewijzigd zijn
        eventPublisher.publishEvent(new EnvironmentChangeEvent(Set.of(
                "spring.datasource.username",
                "spring.datasource.password"
        )));

        // 2. Trigger nu pas de refresh, zodat @ConfigurationProperties en de DataSource opnieuw opbouwen
        contextRefresher.refresh();
    }
}