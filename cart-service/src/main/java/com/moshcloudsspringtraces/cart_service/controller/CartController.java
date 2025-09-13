package com.moshcloudsspringtraces.cart_service.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final RestTemplate restTemplate;

    public CartController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/checkout")
    public ResponseEntity<Map<String, Object>> checkout(@RequestBody Map<String, Object> cartRequest) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now().toString());

        try {
            String orderServiceUrl = "http://localhost:8100/orders/create";
            ResponseEntity<String> responseFromOrder = restTemplate.postForEntity(orderServiceUrl, cartRequest,
                    String.class);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> orderResponseMap = mapper.readValue(responseFromOrder.getBody(), Map.class);

            response.put("status", HttpStatus.OK.value());
            response.put("code", HttpStatusCode.valueOf(200));
            response.put("message", "Checkout initiated");

            Map<String, Object> data = new HashMap<>();
            data.put("service", "cart-service");
             data.put("orderResponse", orderResponseMap);
            response.put("data", data);

            return ResponseEntity.ok(response);

        } catch (ResourceAccessException ex) {
            // Handles connection refused / I/O errors
            response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
            response.put("code", HttpStatusCode.valueOf(503));
            response.put("message", "Order service is currently unavailable");

            Map<String, Object> data = new HashMap<>();
            data.put("service", "cart-service");
            data.put("error", ex.getMessage());
            response.put("data", data);

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        } catch (Exception ex) {
            // Handles any other exceptions
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("code", HttpStatusCode.valueOf(500));
            response.put("message", "Unexpected error occurred");

            Map<String, Object> data = new HashMap<>();
            data.put("service", "cart-service");
            data.put("error", ex.getMessage());
            response.put("data", data);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
