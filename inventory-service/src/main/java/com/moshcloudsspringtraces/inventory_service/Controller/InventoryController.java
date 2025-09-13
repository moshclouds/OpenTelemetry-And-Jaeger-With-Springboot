package com.moshcloudsspringtraces.inventory_service.Controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
     private final RestTemplate restTemplate;

    public InventoryController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

     @PostMapping("/reserve")
    public ResponseEntity<Map<String, Object>> reserveItems(@RequestBody Map<String, Object> inventoryRequest) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now().toString());

        try {
            // Simulate inventory processing
            List<Map<String, Object>> items = (List<Map<String, Object>>) inventoryRequest.get("items");

            response.put("status", HttpStatus.OK.value());
            response.put("code", HttpStatusCode.valueOf(200));
            response.put("message", "Items reserved successfully");

            Map<String, Object> data = new HashMap<>();
            data.put("service", "inventory-service");
            data.put("reservedItems", items);

            response.put("data", data);
            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            // Handle unexpected exceptions
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("code", HttpStatusCode.valueOf(500));
            response.put("message", "Unexpected error occurred");

            Map<String, Object> data = new HashMap<>();
            data.put("service", "inventory-service");
            data.put("error", ex.getMessage());

            response.put("data", data);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
