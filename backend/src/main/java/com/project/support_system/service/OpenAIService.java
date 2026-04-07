package com.project.support_system.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class OpenAIService {

    @Value("${openai.api-key}")
    private String apiKey;

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    public String getAIResponse(String userMessage) {

        RestTemplate restTemplate = new RestTemplate();

        // Request body
        Map<String, Object> request = new HashMap<>();
        request.put("model", "gpt-3.5-turbo");

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", userMessage);

        messages.add(message);
        request.put("messages", messages);

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        // API call
        Map<String, Object> response = restTemplate.postForObject(OPENAI_URL, entity, Map.class);

        if (response == null || !response.containsKey("choices")) {
            return "AI service is currently unavailable.";
        }

        // Parse response
        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        Map<String, Object> choice = choices.get(0);
        Map<String, Object> messageMap = (Map<String, Object>) choice.get("message");

        return (String) messageMap.get("content");
    }
}