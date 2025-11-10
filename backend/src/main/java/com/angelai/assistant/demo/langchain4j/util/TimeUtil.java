package com.angelai.assistant.demo.langchain4j.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TimeUtil {
    
    /**
     * 获取当前时间信息
     * @return 格式化的时间字符串
     */
    public static String getCurrentTimeInfo() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        return now.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss z", Locale.CHINESE));
    }
}