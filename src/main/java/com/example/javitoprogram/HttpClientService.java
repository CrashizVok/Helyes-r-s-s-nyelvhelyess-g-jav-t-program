package com.example.javitoprogram;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.w3c.dom.html.HTMLTableCaptionElement;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class HttpClientService {
    private static final URI CHAT_COMPLETIONS_URI = URI.create("https://api.groq.com/openai/v1/chat/completions");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String SYSTEM_PROMPT = "Javítsd ki a kapott szöveg helyesírási és nyelvtani hibáit és csak a javított szöveget add vissza --> ";

    private final HttpClient httpClient;
    private final String apiKey;
    private final String model;

    public HttpClientService(String apiKey) {
        this(apiKey, "openai/gpt-oss-20b");
    }

    public HttpClientService(String apiKey, String model) {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .build();
        this.apiKey = apiKey == null ? "" : apiKey.trim();
        this.model = model;
    }

    public boolean hasApiKey() {
        return !apiKey.isEmpty();
    }

    public CompletableFuture<String> correctTextAsync(String inputText) {
        try {
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create(String.valueOf(CHAT_COMPLETIONS_URI)))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(buildRequestBody(inputText)))
                    .build();

            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8))
                    .thenApply(this::parseResponseBody);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildRequestBody(String inputText) throws JsonProcessingException {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("model", model);
        body.put("messages", List.of(
                Map.of("role", "system", "content", SYSTEM_PROMPT),
                Map.of("role", "user", "content", inputText)
        ));
        body.put("temperature", 0.2);
        body.put("max_completion_tokens", 2048);
        body.put("top_p", 1);
        body.put("stream", false);
        body.put("reasoning_effort", "medium");
        return OBJECT_MAPPER.writeValueAsString(body);
    }

    private String parseResponseBody(HttpResponse<String> response) {
        try{
            JsonNode root = OBJECT_MAPPER.readTree(response.body());
            return root.path("choices").path(0).path("message").path("contenct").asText("").trim();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }

    }
}































