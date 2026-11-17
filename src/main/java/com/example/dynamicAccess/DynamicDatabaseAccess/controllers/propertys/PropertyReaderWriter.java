package com.example.dynamicAccess.DynamicDatabaseAccess.controllers.propertys;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;


/**
 * Utility component for reading from and writing to properties files located
 * within the Spring application's classpath using the Spring {@link ApplicationContext}.
 */
@RequiredArgsConstructor
@Component
public class PropertyReaderWriter {

    private final  ApplicationContext ctx;

    /**
     * Reads a specific property value from the given properties file.
     *
     * @param property       the key of the property to look up
     * @param propertieFile  the path/name of the properties file relative to the classpath (e.g., "application.properties")
     * @return the value associated with the property key, or {@code null} if the key does not exist
     * @throws IOException if the resource cannot be found, opened, or read
     */
    public String readProperty(String property,String propertieFile) throws IOException {
        Resource resource = ctx.getResource("classpath:"+propertieFile);

        Properties appProps = new Properties();
        appProps.load(resource.getInputStream());

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
     * @param propertieFile  the path/name of the properties file relative to the classpath (e.g., "application.properties")
     * @throws IOException if the resource cannot be found, written to, or if the file cannot be accessed as a writable file on disk
     */
    public void writeProperty(String property,String propertyValue,String propertieFile) throws IOException {
        Resource resource = ctx.getResource("classpath:"+propertieFile);

        Properties appProps = new Properties();
        appProps.setProperty(property,propertyValue);

        appProps.store(new FileWriter(resource.getFile()), "store to properties file");
    }

}
