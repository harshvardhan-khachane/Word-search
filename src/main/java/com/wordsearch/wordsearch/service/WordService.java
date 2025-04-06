package com.wordsearch.wordsearch.service;

import com.wordsearch.wordsearch.model.Trie;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        return trie.searchPrefix(prefix);
    }
}
