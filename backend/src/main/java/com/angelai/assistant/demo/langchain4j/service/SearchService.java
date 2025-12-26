package com.angelai.assistant.demo.langchain4j.service;

import dev.langchain4j.web.search.WebSearchEngine;
import dev.langchain4j.web.search.WebSearchResults;
import dev.langchain4j.web.search.searchapi.SearchApiWebSearchEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SearchService {
    
    @Value("${langchain4j.search-api.api-key}")
    private String searchApiKey;
    
    private WebSearchEngine searchEngine;
    
    public SearchService() {
        // 延迟初始化searchEngine
    }
    
    // 初始化searchEngine的方法
    private void initSearchEngine() {
        if (searchEngine == null) {
            this.searchEngine = SearchApiWebSearchEngine.builder()
                    .apiKey(searchApiKey)
                    .build();
        }
    }
    
    /**
     * 执行网络搜索
     * @param query 搜索查询词
     * @return 搜索结果对象
     */
    public WebSearchResults search(String query) {
        initSearchEngine();
        return searchEngine.search(query);
    }
    
    /**
     * 将搜索结果转换为字符串格式，用于RAG
     * @param results 搜索结果对象
     * @return 格式化的搜索结果字符串
     */
    public String formatSearchResults(WebSearchResults results) {
        StringBuilder sb = new StringBuilder();
        sb.append("网络搜索结果：\n");
        
        // 简单处理，直接将结果转换为字符串
        sb.append(results.toString());
        
        return sb.toString();
    }
}