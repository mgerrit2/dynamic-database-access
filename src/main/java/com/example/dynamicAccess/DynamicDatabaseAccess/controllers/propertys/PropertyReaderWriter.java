package com.example.dynamicAccess.DynamicDatabaseAccess.controllers.propertys;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;


/**
 * Utility component for reading from and writing to properties files located
 * within the Spring application's classpath using the Spring {@link ApplicationContext}.
 * remarks: not needed use /update-credentials
 */
@RequiredArgsConstructor
@Component
public class PropertyReaderWriter {

    private final  ApplicationContext ctx;

    /**
     * Reads a specific property value from the given properties file.
     *
     * @param property       the key of the property to look up
     * @param propertiesFile  the path/name of the properties file relative to the classpath (e.g., "application.properties")
     * @return the value associated with the property key, or {@code null} if the key does not exist
     * @throws IOException if the resource cannot be found, opened, or read
     */
    public String readProperty(String property,String propertiesFile) throws IOException {
        Resource resource = ctx.getResource("classpath:" + propertiesFile);

        Properties appProps = new Properties();
        try (InputStream in = resource.getInputStream()) {
            appProps.load(in);
        }

        return appProps.getProperty(property);
    }

    /**
     * Writes or updates a property value in the given properties file.
     * <p>
     * <b>Note:</b> Writing to a classpath resource via {@code resource.getFile()} may fail
     * if the application is running inside a packaged JAR file rather than an exploded directory.
     *
     * @param property       the key of the property to set
     * @param propertyValue  the value to assign to the property
     * @param propertiesFile  the path/name of the properties file relative to the classpath (e.g., "application.properties")
     * @throws IOException if the resource cannot be found, written to, or if the file cannot be accessed as a writable file on disk
     */
    public void writeProperty(String property,String propertyValue,String propertiesFile) throws IOException {
        Resource resource = ctx.getResource("classpath:" + propertiesFile);
        Path targetPath = resource.getFile().toPath();

        Properties appProps = new Properties();
        if (Files.exists(targetPath)) {
            try (InputStream in = Files.newInputStream(targetPath)) {
                appProps.load(in);
            }
        }

        appProps.setProperty(property, propertyValue);

        try (OutputStream out = Files.newOutputStream(targetPath)) {
            appProps.store(out, "store to properties file");
        }
    }

}
