package com.example.dynamicAccess.DynamicDatabaseAccess.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DatabaseProperties {

    private String url;
    private String username;
    private String password;
    private String driverClassName;
}
