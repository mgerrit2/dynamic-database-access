### Purpose to demonstrate dynamic datasource access
- If the datasource is disconnected, run springboot anyway.
- Connect to more datasource at the same time.
- Restart and shutdown springboot with jvm stays alive from controller
- Dynamically updates the database credentials

## Refresh property with controller(Dynamically updates the database credentials)

## Overview
![dynamic-database-access.bmp](image/dynamic-database-access.bmp)

### The controller
``` java
package com.example.dynamicAccess.DynamicDatabaseAccess.controllers.propertys;

import com.example.dynamicAccess.DynamicDatabaseAccess.DynamicDatabaseAccessApplication;
import com.example.dynamicAccess.DynamicDatabaseAccess.config.DatabaseRefreshConfig;
import com.example.dynamicAccess.DynamicDatabaseAccess.dto.DatabaseProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;


@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/DynamicProperty")
public class DynamicProperty {

    private final DatabaseRefreshConfig databaseRefreshConfig;
    private final Environment env;
    private final ApplicationContext ctx;
    private final PropertyReaderWriter propertyReaderWriter;

    private static final  String APPLICATION_PROPERTIES = "application.properties";
    private static final  String SPRING_DATASOURCE_PASSWORD = "spring.datasource.password";
    private static final  String RESPONSE_CODE_200 = "200";
    private static final  String RESPONSE_CODE_4XX = "4XX";
    private static final  String CLIENT_ERROR = "Client Error (e.g., Bad Request, Unauthorized, Not Found, Validation)";
    private static final  String INTERNAL_SERVER_ERROR = "500";
    private static final  String INTERNAL_SERVER_ERROR_STRING = "Internal Server Error";
    private static final  String UNEXPECTED_ERROR = "Unexpected Error";
    private static final  String DEFAULT = "default";


    @Operation(
            summary = "Flexible endpoint to manually set a different database user and password",
         responses = {
            @ApiResponse(
                    responseCode = RESPONSE_CODE_200,
                    description = "OK",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = RESPONSE_CODE_4XX, // Catches 400, 401, 403, 404, etc.
                    description = CLIENT_ERROR,
                    content = @Content(
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR,
                    description = INTERNAL_SERVER_ERROR_STRING,
                    content = @Content(
                            schema = @Schema(
                                    implementation = ProblemDetail.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = DEFAULT,
                    description = UNEXPECTED_ERROR,
                    content = @Content(
                            schema = @Schema(
                                    implementation = ProblemDetail.class
                            )
                    )
            )
    })
    @PostMapping("/update-credentials")
    public ResponseEntity<String> updateDatabaseCredentials(
            @RequestParam String username,
            @RequestParam String password) {
        try {
            databaseRefreshConfig.updateDatabaseCredentials(username, password);
            return ResponseEntity.ok("Successfully updated database credentials and refreshed connection pool for user: " + username);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to update database credentials: " + e.getMessage());
        }
    }
}
```
### the configuration
``` java
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
```

## Refresh properties


The dependency to add 
``` groovy
implementation 'org.springframework.boot:spring-boot-starter-actuator'
implementation 'org.springframework.cloud:spring-cloud-context:5.0.3'
```

The properties file to insert
``` properties
#######################################################################
# actuators
#######################################################################
# Expose health and refresh endpoints over web
management.endpoints.web.exposure.include=health,refresh,info

# Explicitly enable the refresh endpoint (if required)
management.endpoint.refresh.enabled=true
```


## temp
<div>
<a href="https://stackoverflow.com/questions/57224715/dynamic-datasource-using-springboot">
Dynamic Datasource using SpringBoot [duplicate]
</a>
</div>



## Datasource dynamicaly
<a href="https://www.baeldung.com/spring-boot-configure-data-source-programmatic">
https://www.baeldung.com/spring-boot-configure-data-source-programmatic
</a>

## Restart
<a href="https://www.baeldung.com/java-restart-spring-boot-app">
Programmatically Restarting a Spring Boot Application
</a>


## Reloading Properties Files in
<a href="https://www.baeldung.com/spring-reloading-properties#:~:text=To%20change%20properties%20in%20a,%2F%7Bpath%20to%20file%7D.">
Reloading Properties Files in
</a>

## Run deamon in sprinboot
<a href="https://stackoverflow.com/questions/40941845/how-to-start-and-eventually-stop-a-daemon-thread-in-spring-boot">
Run deamon in sprinboot
</a>

## Enable disable endpoints(stop request)
<a href="https://www.baeldung.com/spring-boot-enable-disable-endpoints-at-runtime">Enable and Disable Endpoints at Runtime With Spring Boot</a>



## Stop request
<a href="https://www.appsloveworld.com/springmvc/100/11/how-do-i-prevent-a-spring-boot-webapplication-from-accepting-http-requests-while?utm_content=cmp-true">HOW DO I PREVENT A SPRING BOOT WEBAPPLICATION FROM ACCEPTING HTTP REQUESTS WHILE IT IS STILL STARTING UP</a>

## Hikori datasource
<a href="https://www.baeldung.com/spring-boot-hikari#:~:text=Hikari%20is%20a%20JDBC%20DataSource,to%20Hikari%2C%20see%20this%20article.">Configuring a Hikari Connection</a>

## Scheduler
<a href="https://stackoverflow.com/questions/35169161/scheduler-not-running-in-spring-boot">Configuring a Hikari Connection</a>


## make sqlserver user

<a href="https://www.youtube.com/watch?v=dJ6c3OgIVDM">Make sqlserver user video</a>

userid:DynamicDatabaseAccess

password:DynamicDatabaseAccessAdmin


## Swagger
http://localhost:8080/swagger-ui/index.html

## actuators
http://localhost:8080/actuator/health
http://localhost:8080/actuator/info
http://localhost:8080/actuator/refresh
