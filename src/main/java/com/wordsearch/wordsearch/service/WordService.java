package com.wordsearch.wordsearch.service;

import com.wordsearch.wordsearch.model.Trie;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WordService {
    private final Trie trie = new Trie();
    private final Map<String, Integer> wordRanks = new ConcurrentHashMap<>();

    // Function to load words from a list(To be called at startup)
    public void loadWords(List<String> words) {
        Set<String> uniqueWords = new HashSet<>(words);
        uniqueWords.forEach(word -> {
            trie.insert(word);
            wordRanks.put(word, 0);
        });
    }

    // Function to search words for given prefix
    public List<String> autoComplete(String prefix) {
        List<String> words = trie.searchPrefix(prefix);

        words.sort(Comparator.comparingInt((String word) -> wordRanks.getOrDefault(word, 0)).reversed());
        return words;
    }

    // Functions to serach word, increment rank and get rank
    public int incrementRank(String word) {
        return wordRanks.computeIfPresent(word, (k, v) -> v + 1);
    }

    public int getRank(String word) {
        return wordRanks.getOrDefault(word, -1);
    }

    public boolean search(String word) {
        return wordRanks.containsKey(word);
    }

    // Function to insert a new word
    public void insertWord(String word) {
        trie.insert(word);
        wordRanks.putIfAbsent(word, 0);
    }
}
