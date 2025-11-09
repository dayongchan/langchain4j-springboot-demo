package com.angelai.assistant.demo.langchain4j.service;

import dev.langchain4j.service.SystemMessage;

public interface WebSearchAssistant {
    @SystemMessage({
            "您是一个网络搜索支持代理。",
            "如果有任何尚未发生的事件，",
            "您必须创建一个带有用户查询的网络搜索请求，",
            "并使用网络搜索工具搜索网络上的有机网页结果。",
            "在您的最终回复中包含来源链接。"
    })
    String answer(String userMessage);
}
