package com.moshcloudsspringtraces.order_service.Controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final RestTemplate restTemplate;

    public OrderController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody Map<String, Object> orderRequest) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now().toString());

        try {
            String inventoryServiceUrl = "http://localhost:8900/inventory/reserve";
            ResponseEntity<String> inventoryResponse =
                    restTemplate.postForEntity(inventoryServiceUrl, orderRequest, String.class);

            String orderId = UUID.randomUUID().toString();

            response.put("status", HttpStatus.OK.value());
            response.put("code", HttpStatusCode.valueOf(200));
            response.put("message", "Order created successfully");

            Map<String, Object> data = new HashMap<>();
            data.put("orderId", orderId);
            data.put("service", "order-service");
            data.put("inventoryResponse", inventoryResponse.getBody());
            response.put("data", data);

            return ResponseEntity.ok(response);

        } catch (ResourceAccessException ex) {
            response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
            response.put("code", HttpStatusCode.valueOf(503));
            response.put("message", "Inventory service is currently unavailable");

            Map<String, Object> data = new HashMap<>();
            data.put("service", "order-service");
            data.put("error", ex.getMessage());
            response.put("data", data);

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        } catch (Exception ex) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("code", HttpStatusCode.valueOf(500));
            response.put("message", "Unexpected error occurred");

            Map<String, Object> data = new HashMap<>();
            data.put("service", "order-service");
            data.put("error", ex.getMessage());
            response.put("data", data);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
