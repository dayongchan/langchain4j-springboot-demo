package com.angelai.assistant.demo.langchain4j.service;

import dev.langchain4j.web.search.WebSearchEngine;
import dev.langchain4j.web.search.WebSearchResults;
import dev.langchain4j.web.search.searchapi.SearchApiWebSearchEngine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private WebSearchEngine mockSearchEngine;
    
    @Mock
    private WebSearchResults mockResults;

    @InjectMocks
    private SearchService searchService;

    @Test
    void testSearch() {
        // Given
        String query = "test query";
        
        // 设置必要的属性
        ReflectionTestUtils.setField(searchService, "searchApiKey", "test-api-key");
        ReflectionTestUtils.setField(searchService, "searchEngine", mockSearchEngine);
        
        when(mockSearchEngine.search(query)).thenReturn(mockResults);
        
        // When
        WebSearchResults results = searchService.search(query);
        
        // Then
        assertEquals(mockResults, results);
    }

    @Test
    void testFormatSearchResults() {
        // Given
        String mockResultsString = "test search results";
        when(mockResults.toString()).thenReturn(mockResultsString);
        
        // 设置必要的属性
        ReflectionTestUtils.setField(searchService, "searchApiKey", "test-api-key");
        ReflectionTestUtils.setField(searchService, "searchEngine", mockSearchEngine);
        
        // When
        String formattedResults = searchService.formatSearchResults(mockResults);
        
        // Then
        assertEquals("网络搜索结果：\n" + mockResultsString, formattedResults);
    }
}