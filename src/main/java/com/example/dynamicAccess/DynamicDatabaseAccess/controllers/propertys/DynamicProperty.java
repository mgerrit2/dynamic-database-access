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



    @Operation(summary = "Restarts the entire Spring Boot application without killing the host JVM process",responses = {
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
    @PostMapping("/restart")
    public ResponseEntity<String> restart(){

        DynamicDatabaseAccessApplication.restart();

        return ResponseEntity.ok("restart service");

    }

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

    @Operation(
            summary = "Reset DataSource user to SA",
            description = "Updates the datasource username and password configuration properties to default 'sa' credentials."
            ,responses = {
            @ApiResponse(
                    responseCode = RESPONSE_CODE_200,
                    description = "OK",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            schema = @Schema(implementation = String.class)
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
    @PostMapping("/changePropertyUserSA")
    public ResponseEntity<?> changePropertyUserSA() throws IOException {

        propertyReaderWriter.writeProperty(SPRING_DATASOURCE_PASSWORD,"DynamicDatabaseAccess",APPLICATION_PROPERTIES);
        propertyReaderWriter.writeProperty(SPRING_DATASOURCE_PASSWORD,"DynamicDatabaseAccessAdmin",APPLICATION_PROPERTIES);


        return ResponseEntity.ok("property writen:" + propertyReaderWriter.readProperty(SPRING_DATASOURCE_PASSWORD,APPLICATION_PROPERTIES) );
    }

    @Operation(
            summary = "Reset DataSource user to SA",
            description = "Updates the datasource username and password configuration properties to default 'sa' credentials.",responses = {
            @ApiResponse(
                    responseCode = RESPONSE_CODE_200,
                    description = "OK",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            schema = @Schema(implementation = String.class)
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
    @PostMapping("/changePropertyUserSABack")
    public ResponseEntity<String> changePropertyUserSABack() throws IOException {

        propertyReaderWriter.writeProperty("spring.datasource.username","sa",APPLICATION_PROPERTIES);
        propertyReaderWriter.writeProperty("spring.datasource.password","poseidon16011963",APPLICATION_PROPERTIES);


        return ResponseEntity.ok("property writen:" + propertyReaderWriter.readProperty(SPRING_DATASOURCE_PASSWORD,APPLICATION_PROPERTIES) );
    }

    @Operation(summary = "Safely closes the active Spring IoC container",responses = {
            @ApiResponse(
                    responseCode = RESPONSE_CODE_200,
                    description = "OK",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            schema = @Schema(implementation = String.class)
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
    @PostMapping("/shutdown")
    public ResponseEntity<String> shutdown(){
        DynamicDatabaseAccessApplication.shutdown();
        return ResponseEntity.ok("shutdown");
    }


    @Operation(summary = "Get the propertie files",responses = {
            @ApiResponse(
                    responseCode = RESPONSE_CODE_200,
                    description = "OK",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = DatabaseProperties.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "4XX", // Catches 400, 401, 403, 404, etc.
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
    @GetMapping("/getpropertys")
    public ResponseEntity<DatabaseProperties> getpropertys() throws IOException {

        var databaseProperties = DatabaseProperties.builder()
                .url(env.getProperty("spring.datasource.url"))
                .username(env.getProperty("spring.datasource.username"))
                .password(env.getProperty("spring.datasource.password"))
                .driverClassName(env.getProperty("spring.datasource.driverClassName"))
                .build();


        return ResponseEntity.ok(databaseProperties);
    }

}
