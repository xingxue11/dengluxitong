package com.community.aipredictservice.config;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

import com.community.aipredictservice.enums.ModelType;

@Configuration
public class AiModelConfig {

    @Value("${ai.moonshot.api-key:}")
    private String moonshotApiKey;

    @Value("${ai.moonshot.base-url:https://api.moonshot.cn/v1}")
    private String moonshotBaseUrl;
    
    @Value("${ai.moonshot.model:moonshot-v1-8k}")
    private String moonshotModel;

    @Value("${ai.qwen.api-key:}")
    private String qwenApiKey;

    @Value("${ai.qwen.base-url:https://dashscope.aliyuncs.com/compatible-mode/v1}")
    private String qwenBaseUrl;
    
    @Value("${ai.qwen.model:qwen-plus}")
    private String qwenModel;

    @Value("${ai.zhipu.api-key:}")
    private String zhipuApiKey;

    @Value("${ai.zhipu.base-url:https://open.bigmodel.cn/api/paas/v4}")
    private String zhipuBaseUrl;
    
    @Value("${ai.zhipu.model:glm-4}")
    private String zhipuModel;

    @Value("${ai.doubao.api-key:}")
    private String doubaoApiKey;

    @Value("${ai.doubao.base-url:https://ark.cn-beijing.volces.com/api/v3}")
    private String doubaoBaseUrl;
    
    @Value("${ai.doubao.model:doubao-seed-1-6-thinking-250715}")
    private String doubaoModel;

    @Value("${ai.local.base-url:http://localhost:11434}")
    private String localBaseUrl;
    
    @Value("${ai.local.model:llama3}")
    private String localModel;
    
    @Value("${ai.local.enabled:true}")
    private boolean localEnabled;

    @Bean("kimiChatClient")
    public ChatClient kimiChatClient() {
        if (moonshotApiKey == null || moonshotApiKey.isEmpty()) {
            return null;
        }
        OpenAiApi api = new OpenAiApi(moonshotBaseUrl, moonshotApiKey, RestClient.builder(), WebClient.builder());

        // 2. 创建 OpenAiChatModel，并设置默认选项（模型名称等）
        OpenAiChatModel chatModel = new OpenAiChatModel(api,
                OpenAiChatOptions.builder()
                        .withModel(qwenModel)
                        .build()
        );

        // 3. 通过 ChatClient.Builder 构建 ChatClient
        return ChatClient.builder(chatModel).build();
    }

    @Bean("doubaoChatClient")
    public ChatClient doubaoChatClient() {
        if (doubaoApiKey == null || doubaoApiKey.isEmpty()) {
            return null;
        }
        OpenAiApi api = new OpenAiApi(doubaoBaseUrl, doubaoApiKey,"/chat/completions","/v1/embeddings", RestClient.builder(), WebClient.builder(),RetryUtils.DEFAULT_RESPONSE_ERROR_HANDLER);
        OpenAiChatModel chatModel = new OpenAiChatModel(api,
                OpenAiChatOptions.builder()
                        .withModel(doubaoModel)
                        .build()
        );

        // 3. 通过 ChatClient.Builder 构建 ChatClient
        return ChatClient.builder(chatModel).build();
    }

    @Bean("qianwenChatClient")
    public ChatClient qianwenChatClient() {
        if (qwenApiKey == null || qwenApiKey.isEmpty()) {
            return null;
        }
        
        
     // 1. 创建 OpenAiApi 实例（注意使用构建好的 RestClient/WebClient）

        OpenAiApi api = new OpenAiApi(qwenBaseUrl, qwenApiKey, RestClient.builder(), WebClient.builder());

        // 2. 创建 OpenAiChatModel，并设置默认选项（模型名称等）
        OpenAiChatModel chatModel = new OpenAiChatModel(api,
                OpenAiChatOptions.builder()
                        .withModel(qwenModel)
                        .build()
        );

        // 3. 通过 ChatClient.Builder 构建 ChatClient
        return ChatClient.builder(chatModel).build();
       
    }

    @Bean("zhipuChatClient")
    public ChatClient zhipuChatClient() {
        if (zhipuApiKey == null || zhipuApiKey.isEmpty()) {
            return null;
        }
        OpenAiApi api = new OpenAiApi(zhipuBaseUrl, zhipuApiKey, RestClient.builder(), WebClient.builder());

        OpenAiChatModel chatModel = new OpenAiChatModel(api,
                OpenAiChatOptions.builder()
                        .withModel(zhipuModel)
                        .build()
        );

        return ChatClient.builder(chatModel).build();
    }

    @Bean("localChatClient")
    public ChatClient localChatClient() {
        if (!localEnabled) {
            return null;
        }

        
        OpenAiApi api = new OpenAiApi(localBaseUrl, "ollama","/api/chat","/v1/embeddings", RestClient.builder(), WebClient.builder(),RetryUtils.DEFAULT_RESPONSE_ERROR_HANDLER);
        OpenAiChatModel chatModel = new OpenAiChatModel(api,
                OpenAiChatOptions.builder()
                        .withModel(doubaoModel)
                        .build()
        );
        

        return ChatClient.builder(chatModel).build();
    }

    @Bean
    public Map<ModelType, ChatClient> chatClientMap() {
        Map<ModelType, ChatClient> map = new EnumMap<>(ModelType.class);
        
        if (moonshotApiKey != null && !moonshotApiKey.isEmpty()) {
            OpenAiApi api = new OpenAiApi(moonshotBaseUrl, moonshotApiKey, RestClient.builder(), WebClient.builder());
            // 2. 创建 OpenAiChatModel，并设置默认选项（模型名称等）
            OpenAiChatModel chatModel = new OpenAiChatModel(api,
                    OpenAiChatOptions.builder()
                            .withModel(moonshotModel)
                            .build()
            );

       
            map.put(ModelType.KIMI,ChatClient.builder(chatModel).build());
        }
        if (doubaoApiKey != null && !doubaoApiKey.isEmpty()) {
        	
        	
			/*
			 * public OpenAiApi(doubaoBaseUrl, String apiKey, String completionsPath, String
			 * embeddingsPath, RestClient.Builder restClientBuilder, WebClient.Builder
			 * webClientBuilder, ResponseErrorHandler responseErrorHandler)
			 */
            OpenAiApi api = new OpenAiApi(doubaoBaseUrl, doubaoApiKey,"/chat/completions","/v1/embeddings", RestClient.builder(), WebClient.builder(),RetryUtils.DEFAULT_RESPONSE_ERROR_HANDLER);
            OpenAiChatModel chatModel = new OpenAiChatModel(api,
                    OpenAiChatOptions.builder()
                            .withModel(doubaoModel)
                            .build()
            );

            map.put(ModelType.DOUBAO, ChatClient.builder(chatModel).build());
        }
        if (qwenApiKey != null && !qwenApiKey.isEmpty()) {
            OpenAiApi api = new OpenAiApi(qwenBaseUrl, qwenApiKey, RestClient.builder(), WebClient.builder());
            OpenAiChatModel chatModel = new OpenAiChatModel(api,
                    OpenAiChatOptions.builder()
                            .withModel(qwenModel)
                            .build()
            );
            map.put(ModelType.QIANWEN,ChatClient.builder(chatModel).build());
        }
        if (zhipuApiKey != null && !zhipuApiKey.isEmpty()) {
            OpenAiApi api = new OpenAiApi(zhipuBaseUrl, zhipuApiKey, RestClient.builder(), WebClient.builder());
            OpenAiChatModel chatModel = new OpenAiChatModel(api,
                    OpenAiChatOptions.builder()
                            .withModel(zhipuModel)
                            .build()
            );
            map.put(ModelType.ZHIPU, ChatClient.builder(chatModel).build());
        }
        if (localEnabled) {
            OpenAiApi api = new OpenAiApi(localBaseUrl, "ollama", RestClient.builder(), WebClient.builder());
            OpenAiChatModel chatModel = new OpenAiChatModel(api,
                    OpenAiChatOptions.builder()
                            .withModel(localModel)
                            .build()
            );
            map.put(ModelType.LOCAL, ChatClient.builder(chatModel).build());
        }
        
        return map;
    }

    @Bean
    public Map<ModelType, String> modelBaseUrlMap() {
        Map<ModelType, String> map = new EnumMap<>(ModelType.class);
        if (moonshotApiKey != null && !moonshotApiKey.isEmpty()) {
            map.put(ModelType.KIMI, moonshotBaseUrl);
        }
        if (doubaoApiKey != null && !doubaoApiKey.isEmpty()) {
            map.put(ModelType.DOUBAO, doubaoBaseUrl);
        }
        if (qwenApiKey != null && !qwenApiKey.isEmpty()) {
            map.put(ModelType.QIANWEN, qwenBaseUrl);
        }
        if (zhipuApiKey != null && !zhipuApiKey.isEmpty()) {
            map.put(ModelType.ZHIPU, zhipuBaseUrl);
        }
        if (localEnabled) {
            map.put(ModelType.LOCAL, localBaseUrl);
        }
        return map;
    }
}