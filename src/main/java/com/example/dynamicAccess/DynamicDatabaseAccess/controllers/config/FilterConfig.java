package com.example.dynamicAccess.DynamicDatabaseAccess.controllers.config;


import com.example.dynamicAccess.DynamicDatabaseAccess.maintenance.filter.MaintenanceFilter;
import com.example.dynamicAccess.DynamicDatabaseAccess.maintenance.service.MaintenanceService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<MaintenanceFilter> maintenanceFilterRegistration(MaintenanceService maintenanceService) {
        FilterRegistrationBean<MaintenanceFilter> reg = new FilterRegistrationBean<>();
        reg.setFilter(new MaintenanceFilter(maintenanceService));
        reg.setOrder(Ordered.HIGHEST_PRECEDENCE);
        reg.addUrlPatterns("/*");
        reg.setName("maintenanceFilter");
        return reg;
    }
}