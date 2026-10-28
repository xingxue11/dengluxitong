package com.community.aipredictservice.service;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("weatherService")
public class WeatherService {

    private static final Map<String, String> WEATHER_DATA = new HashMap<>();

    static {
        WEATHER_DATA.put("北京", "北京，晴，25℃，风力3级");
        WEATHER_DATA.put("上海", "上海，多云，28℃，风力2级");
        WEATHER_DATA.put("广州", "广州，阴转小雨，32℃，风力4级");
        WEATHER_DATA.put("深圳", "深圳，晴，30℃，风力2级");
        WEATHER_DATA.put("杭州", "杭州，多云转晴，26℃，风力3级");
        WEATHER_DATA.put("成都", "成都，小雨，22℃，风力1级");
        WEATHER_DATA.put("重庆", "重庆，阴天，24℃，风力2级");
        WEATHER_DATA.put("武汉", "武汉，晴转多云，27℃，风力3级");
        WEATHER_DATA.put("南京", "南京，晴，25℃，风力2级");
        WEATHER_DATA.put("西安", "西安，晴，23℃，风力3级");
        WEATHER_DATA.put("天津", "天津，多云，26℃，风力4级");
        WEATHER_DATA.put("苏州", "苏州，晴，27℃，风力2级");
        WEATHER_DATA.put("郑州", "郑州，晴，24℃，风力3级");
        WEATHER_DATA.put("长沙", "长沙，多云，28℃，风力2级");
        WEATHER_DATA.put("青岛", "青岛，晴，22℃，风力5级");
    }

    public String getWeather(String city) {
        if (city == null || city.trim().isEmpty()) {
            return "请提供城市名称";
        }

        String weather = WEATHER_DATA.get(city.trim());
        if (weather != null) {
            return weather;
        }

        for (Map.Entry<String, String> entry : WEATHER_DATA.entrySet()) {
            if (city.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        return city + "，晴，25℃，风力2级";
    }
}