package com.example.DAR.Service;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.Map;

@Service
public class WorkflowTriggerService {

    private final RestClient restClient = RestClient.create();

    public void sendToN8n(Map<String, Object> payload) {
        String n8nWebhookUrl = "http://localhost:5678/webhook/sensor-analysis";

        restClient.post()
                .uri(n8nWebhookUrl)
                .body(payload)
                .retrieve()
                .toBodilessEntity(); // Fire and forget asynchronously
    }
}