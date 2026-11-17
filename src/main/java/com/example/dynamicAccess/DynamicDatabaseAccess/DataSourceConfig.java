package com.example.dynamicAccess.DynamicDatabaseAccess;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;

import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@RequiredArgsConstructor
//@Configuration
public class DataSourceConfig {

    private final Environment env;

    @Bean
    @ConditionalOnProperty(
            name = "datasourceActive",
            havingValue = "true")
    @ConditionalOnMissingBean()
    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSourceBuilder.url("jdbc:sqlserver://localhost;databaseName=AdventureWorksDW2017;trustServerCertificate=true");
        dataSourceBuilder.username("SA");
        dataSourceBuilder.password("poseidon16011963");
        //dataSourceBuilder.password(env.getProperty("spring.datasource.password"));
        //dataSourceBuilder.password("kkk");
        return dataSourceBuilder.build();
    }
}
