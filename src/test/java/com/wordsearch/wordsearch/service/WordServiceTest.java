package com.wordsearch.wordsearch.service;

import com.wordsearch.wordsearch.model.Trie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

public class WordServiceTest {
    private WordService wordService;
    private final int MAX_WORD_LENGTH = 50;
    private final int MAX_PREFIX_LENGTH = 20;

    @BeforeEach
    void setup() {
        wordService = new WordService();
    }

    @Test
    void testWordLengthConstraint() {
        String validWord = "a".repeat(MAX_WORD_LENGTH);
        String invalidWord = "a".repeat(MAX_WORD_LENGTH + 1);

        wordService.insertWord(validWord); // Should work
        assertThrows(IllegalArgumentException.class,
                () -> wordService.insertWord(invalidWord));
    }

    @Test
    void testMaxWordsConstraint() {
        List<String> words = generateWords(100_000, 10);
        wordService.loadWords(words);
        assertEquals(100_000, wordService.autoComplete("").size());
    }

    @Test
    void testInsertAndSearch() {
        wordService.insertWord("apple");
        assertTrue(wordService.search("apple"));
        assertFalse(wordService.search("banana"));
    }

    @Test
    void testAutoCompleteSorting() {
        wordService.insertWord("apple");
        wordService.insertWord("app");
        wordService.incrementRank("apple"); // Rank 1
        wordService.incrementRank("apple"); // Rank 2

        List<String> suggestions = wordService.autoComplete("app");
        assertEquals(Arrays.asList("apple", "app"), suggestions);
    }

    @Test
    void testRankIncrement() {
        wordService.insertWord("hello");
        wordService.incrementRank("hello");
        assertEquals(1, wordService.getRank("hello"));
    }


    @Test
    void testConcurrentRankUpdates() throws InterruptedException {
        wordService.insertWord("test");
        int threads = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        for (int i = 0; i < threads; i++) {
            executor.submit(() -> wordService.incrementRank("test"));
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        assertEquals(threads, wordService.getRank("test"));
    }

    @Test
    void testBulkInsertPerformance() {
        List<String> words = generateWords(100_000, 10); // Generate 100k words of 10 chars
        long startTime = System.currentTimeMillis();
        wordService.loadWords(words);
        long duration = System.currentTimeMillis() - startTime;

        System.out.println("Inserted 100,000 words in " + duration + "ms");
        assertTrue(duration < 5000, "Insertion took too long: " + duration + "ms");
    }

    private List<String> generateWords(int count, int length) {
        List<String> words = new ArrayList<>(count);
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            StringBuilder word = new StringBuilder(length);
            for (int j = 0; j < length; j++) {
                word.append((char) ('a' + random.nextInt(26)));
            }
            words.add(word.toString());
        }
        return words;
    }
}