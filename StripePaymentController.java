package com.rbts.vstate.order.controller.applicationDB;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stripe")
public class StripePaymentController {

    private static final String STRIPE_SECRET_KEY = "sk_test_51PyTdiKhsNfb7YuLVUWFsvA2tYubWbSTskrmwfEYkLdTHvF7Re5uB0Kyi6zIYF7CKVT9iijl0xGYZTw1CQlisvKj00DJO2cmvJ"; // Replace with actual secret key

    /**
     * Create a PaymentIntent with orderId as metadata.
     */
    @PostMapping("/create-payment-intent")
    public ResponseEntity<Object> createPaymentIntent(@RequestParam int amount, @RequestParam String orderId) {
        String url = "https://api.stripe.com/v1/payment_intents";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + STRIPE_SECRET_KEY);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("amount", String.valueOf(amount)); // Amount in cents (e.g., $50.00 = 5000)
        requestBody.add("currency", "usd");
        requestBody.add("payment_method_types[]", "card");
        requestBody.add("metadata[orderId]", orderId);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());

            Map<String, Object> data = new HashMap<>();
            data.put("paymentIntentId", jsonResponse.get("id").asText());
            data.put("status", jsonResponse.get("status").asText());
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating PaymentIntent: " + e.getMessage());
        }
    }

    /**
     * Check if any payment exists for a given orderId.
     */
    @GetMapping("/check-payment/{orderId}")
    public ResponseEntity<Object> checkPaymentByOrderId(@PathVariable String orderId) {
        String url = "https://api.stripe.com/v1/payment_intents";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + STRIPE_SECRET_KEY);

        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());

            JsonNode dataArray = jsonResponse.get("data");
            if (dataArray != null && dataArray.isArray()) {
                for (JsonNode paymentIntent : dataArray) {
                    JsonNode metadata = paymentIntent.get("metadata");
                    if (metadata != null && metadata.has("orderId") && metadata.get("orderId").asText().equals(orderId)) {
                        return ResponseEntity.ok(paymentIntent);
                    }
                }
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No payment found for orderId: " + orderId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error checking payment: " + e.getMessage());
        }
    }
}
