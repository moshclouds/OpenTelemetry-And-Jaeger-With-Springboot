package com.moshcloudsspringtraces.inventory_service.Controller;

import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class StatusController {
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("status", HttpStatus.OK.value());
        response.put("code", HttpStatusCode.valueOf(200));
        response.put("message", "inventory-service online");

        Map<String, Object> data = new HashMap<>();
        data.put("service", "inventory-service");
        data.put("health", "UP");

        response.put("data", data);

        return ResponseEntity.ok(response);
    }

}
