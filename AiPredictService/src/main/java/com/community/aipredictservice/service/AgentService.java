package com.community.aipredictservice.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AgentService {

    private final ChatClient agentChatClient;
    private final WeatherService weatherService;

    private static final Pattern CITY_PATTERN = Pattern.compile(
            "([\\u4e00-\\u9fa5]{2,4})天气|天气([\\u4e00-\\u9fa5]{2,4})|查询([\\u4e00-\\u9fa5]{2,4})天气", 
            Pattern.CASE_INSENSITIVE
    );

    public AgentService(@Qualifier("agentChatClient") ChatClient agentChatClient, WeatherService weatherService) {
        this.agentChatClient = agentChatClient;
        this.weatherService = weatherService;
    }

    public String chat(String msg) {
        String city = extractCity(msg);
        
        if (city != null) {
            String weatherInfo = weatherService.getWeather(city);
            String prompt = String.format(
                "用户问的是'%s'的天气。我已经查到了天气信息：%s。请用自然、友好的语言总结这段天气信息并回复用户。",
                city, weatherInfo
            );
            return agentChatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();
        }
        
        return agentChatClient.prompt()
                .user(msg)
                .call()
                .content();
    }

    private String extractCity(String msg) {
        if (msg == null) {
            return null;
        }
        
        Matcher matcher = CITY_PATTERN.matcher(msg);
        if (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                if (matcher.group(i) != null && !matcher.group(i).isEmpty()) {
                    return matcher.group(i);
                }
            }
        }
        
        String[] commonCities = {"北京", "上海", "广州", "深圳", "杭州", "成都", "重庆", "武汉", "南京", "西安", "天津", "苏州", "郑州", "长沙", "青岛"};
        for (String city : commonCities) {
            if (msg.contains(city)) {
                return city;
            }
        }
        
        return null;
    }
}