package com.angelai.assistant.demo.langchain4j.util;

import com.angelai.assistant.demo.langchain4j.enums.TemperatureUnit;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

/**
 * 给大模型定义的天气预报工具
 */
public class WeatherTools {
    @Tool("返回给定城市的天气预报")
    String getWeather(@P("应返回天气预报的城市") String city, TemperatureUnit temperatureUnit) {
        //TODO 获取天气预报
        return "天气预报：" + city + "的天气是晴天，温度是25度。";
    }
}
