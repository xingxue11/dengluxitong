package com.community.aipredictservice.config;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.community.aipredictservice.enums.ModelType;

@Configuration
public class AiModelConfig {

    @Value("${ai.moonshot.api-key:}")
    private String moonshotApiKey;

    @Value("${ai.moonshot.base-url:https://api.moonshot.cn/v1}")
    private String moonshotBaseUrl;

    @Value("${ai.moonshot.enabled:true}")
    private boolean moonshotEnabled;

    @Value("${ai.qwen.api-key:}")
    private String qwenApiKey;

    @Value("${ai.qwen.base-url:https://dashscope.aliyuncs.com/compatible-mode/v1}")
    private String qwenBaseUrl;

    @Value("${ai.qwen.enabled:true}")
    private boolean qwenEnabled;

    @Value("${ai.zhipu.api-key:}")
    private String zhipuApiKey;

    @Value("${ai.zhipu.base-url:https://open.bigmodel.cn/api/paas/v4}")
    private String zhipuBaseUrl;

    @Value("${ai.zhipu.enabled:true}")
    private boolean zhipuEnabled;

    @Value("${ai.doubao.api-key:}")
    private String doubaoApiKey;

    @Value("${ai.doubao.base-url:https://ark.cn-beijing.volces.com/api/v3}")
    private String doubaoBaseUrl;

    @Value("${ai.doubao.enabled:true}")
    private boolean doubaoEnabled;

    @Bean("kimiChatClient")
    public ChatClient kimiChatClient() {
        if (!moonshotEnabled || moonshotApiKey == null || moonshotApiKey.isEmpty()) {
            return null;
        }
        return ChatClient.create(new OpenAiChatModel(new OpenAiApi(moonshotBaseUrl, moonshotApiKey)));
    }

    @Bean("doubaoChatClient")
    public ChatClient doubaoChatClient() {
        if (!doubaoEnabled || doubaoApiKey == null || doubaoApiKey.isEmpty()) {
            return null;
        }
        return ChatClient.create(new OpenAiChatModel(new OpenAiApi(doubaoBaseUrl, doubaoApiKey)));
    }

    @Bean("qianwenChatClient")
    public ChatClient qianwenChatClient() {
        if (!qwenEnabled || qwenApiKey == null || qwenApiKey.isEmpty()) {
            return null;
        }
        return ChatClient.create(new OpenAiChatModel(new OpenAiApi(qwenBaseUrl, qwenApiKey)));
    }

    @Bean("zhipuChatClient")
    public ChatClient zhipuChatClient() {
        if (!zhipuEnabled || zhipuApiKey == null || zhipuApiKey.isEmpty()) {
            return null;
        }
        return ChatClient.create(new OpenAiChatModel(new OpenAiApi(zhipuBaseUrl, zhipuApiKey)));
    }

    @Bean
    public Map<ModelType, ChatClient> chatClientMap(
            @Qualifier("kimiChatClient") ChatClient kimiChatClient,
            @Qualifier("doubaoChatClient") ChatClient doubaoChatClient,
            @Qualifier("qianwenChatClient") ChatClient qianwenChatClient,
            @Qualifier("zhipuChatClient") ChatClient zhipuChatClient) {

        Map<ModelType, ChatClient> map = new EnumMap<>(ModelType.class);
        if (kimiChatClient != null) {
            map.put(ModelType.KIMI, kimiChatClient);
        }
        if (doubaoChatClient != null) {
            map.put(ModelType.DOUBAO, doubaoChatClient);
        }
        if (qianwenChatClient != null) {
            map.put(ModelType.QIANWEN, qianwenChatClient);
        }
        if (zhipuChatClient != null) {
            map.put(ModelType.ZHIPU, zhipuChatClient);
        }
        return map;
    }
}