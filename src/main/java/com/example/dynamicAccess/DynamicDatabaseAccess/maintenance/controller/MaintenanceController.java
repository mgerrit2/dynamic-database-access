package com.example.dynamicAccess.DynamicDatabaseAccess.maintenance.controller;

import com.example.dynamicAccess.DynamicDatabaseAccess.maintenance.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/admin/maintenance")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @PostMapping("/enable")
    public ResponseEntity<String> enableMaintenance() {
        maintenanceService.setEnabled(true);
        return ResponseEntity.ok("Maintenance mode ENABLED. All traffic is now blocked.");
    }

    @PostMapping("/disable")
    public ResponseEntity<String> disableMaintenance() {
        maintenanceService.setEnabled(false);
        return ResponseEntity.ok("Maintenance mode DISABLED. Normal traffic resumed.");
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        boolean active = maintenanceService.isEnabled();
        return ResponseEntity.ok("Maintenance mode is currently: " + (active ? "ACTIVE" : "INACTIVE"));
    }
}