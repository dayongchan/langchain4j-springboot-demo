package com.angelai.assistant.demo.langchain4j.service.llm;

import com.angelai.assistant.demo.langchain4j.enums.Sentiment;
import dev.langchain4j.service.UserMessage;

/**
 * 情感分析
 *
 */
public interface SentimentAnalyzer {
    @UserMessage("Analyze sentiment of {{it}}")
    Sentiment analyzeSentimentOf(String text);

    @UserMessage("Does {{it}} have a positive sentiment?")
    boolean isPositive(String text);
}
