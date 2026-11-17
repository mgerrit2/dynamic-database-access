package com.example.dynamicAccess.DynamicDatabaseAccess.maintenance.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class MaintenanceService {

    private final AtomicBoolean maintenanceMode = new AtomicBoolean(false);

    public boolean isEnabled() {
        return maintenanceMode.get();
    }

    public void setEnabled(boolean enabled) {
        maintenanceMode.set(enabled);
    }
}