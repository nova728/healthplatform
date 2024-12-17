package com.health.healthplatform.service;

import com.health.healthplatform.DTO.ChatRequest;
import com.health.healthplatform.DTO.ChatResponse;
import com.health.healthplatform.config.ErnieBotConfig;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ChatService {

    private final OkHttpClient httpClient;

    @Autowired
    private ErnieBotConfig ernieBotConfig;

    public ChatService() {
        // 配置 OkHttpClient 的超时时间
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)      // 连接超时
                .writeTimeout(30, TimeUnit.SECONDS)        // 写入超时
                .readTimeout(30, TimeUnit.SECONDS)         // 读取超时
                .build();
    }

    public ChatResponse processMessage(ChatRequest request) {
        try {
            String accessToken = getAccessToken();
            String response = callErnieBot(request.getMessage(), accessToken);
            return new ChatResponse(response);
        } catch (IOException e) {
            log.error("Error processing message: ", e);
            throw new RuntimeException("Failed to process message: " + e.getMessage());
        }
    }

    private String getAccessToken() throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType,
                "grant_type=client_credentials&client_id=" + ernieBotConfig.getApiKey() +
                        "&client_secret=" + ernieBotConfig.getSecretKey());

        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "No error body";
                log.error("Failed to get access token. Status: {}, Error: {}", response.code(), errorBody);
                throw new IOException("Failed to get access token: " + response.code());
            }
            JSONObject jsonResponse = new JSONObject(response.body().string());
            return jsonResponse.getString("access_token");
        }
    }

    private String callErnieBot(String message, String accessToken) throws IOException {
        log.debug("Calling Ernie Bot with message: {}", message);

        MediaType mediaType = MediaType.parse("application/json");
        JSONObject requestBody = new JSONObject()
                .put("messages", new JSONObject[]{
                        new JSONObject()
                                .put("role", "user")
                                .put("content", message)
                })
                .put("temperature", 0.95)
                .put("top_p", 0.8)
                .put("penalty_score", 1)
                .put("stream", false);

        RequestBody body = RequestBody.create(mediaType, requestBody.toString());

        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions_pro?access_token=" + accessToken)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "No error body";
                log.error("Failed to call Ernie Bot. Status: {}, Error: {}", response.code(), errorBody);
                throw new IOException("Failed to call Ernie Bot: " + response.code());
            }
            JSONObject jsonResponse = new JSONObject(response.body().string());
            return jsonResponse.getString("result");
        }
    }
}