package com.wordsearch.wordsearch.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class WordControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testInsertEndpoint() throws Exception {
        mockMvc.perform(post("/api/insert?word=test"))
                .andExpect(status().isOk())
                .andExpect(content().string("Word inserted: test"));
    }

    @Test
    void testAutoCompleteEndpoint() throws Exception {
        mockMvc.perform(post("/api/insert?word=apple"));
        mockMvc.perform(post("/api/insert?word=app"));
        mockMvc.perform(post("/api/search?word=apple")); // Set apple's rank to 1

        mockMvc.perform(get("/api/auto-complete?prefix=app"))
                .andExpect(jsonPath("$[0]").value("apple"))
                .andExpect(jsonPath("$[1]").value("app"));
    }

    @Test
    void testRankEndpoint() throws Exception {
        mockMvc.perform(post("/api/insert?word=hello"));
        mockMvc.perform(post("/api/search?word=hello"));

        mockMvc.perform(get("/api/rank?word=hello"))
                .andExpect(jsonPath("$.rank").value(1));
    }

    @Test
    void testInvalidWordLength() throws Exception {
        String invalidWord = "a".repeat(51);
        mockMvc.perform(post("/api/insert?word=" + invalidWord))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLongPrefixHandling() throws Exception {
        String longPrefix = "a".repeat(21);
        mockMvc.perform(get("/api/auto-complete?prefix=" + longPrefix))
                .andExpect(status().isBadRequest());
    }
}