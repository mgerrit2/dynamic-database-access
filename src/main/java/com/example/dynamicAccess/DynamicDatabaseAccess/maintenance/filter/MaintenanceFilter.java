package com.example.dynamicAccess.DynamicDatabaseAccess.maintenance.filter;

import com.example.dynamicAccess.DynamicDatabaseAccess.maintenance.service.MaintenanceService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class MaintenanceFilter extends OncePerRequestFilter {

    private final MaintenanceService maintenanceService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // 1. Always allow health checks, actuator, and the admin maintenance toggle endpoints!
        if (
                path.startsWith("/actuator/") ||
                path.startsWith("/api/v1/admin/maintenance") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Check if maintenance mode is active
        if (maintenanceService.isEnabled()) {
            response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"System is under maintenance. Please try again later.\"}");
            return;
        }

        // 3. Normal flow
        filterChain.doFilter(request, response);
    }
}