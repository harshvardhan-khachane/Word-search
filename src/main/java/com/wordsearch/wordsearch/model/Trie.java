package com.wordsearch.wordsearch.model;

import java.util.ArrayList;
import java.util.List;

public class Trie {
    private TrieNode root = new TrieNode();

    public synchronized void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node.getChildren().putIfAbsent(c, new TrieNode());
            node = node.getChildren().get(c);
        }
        node.setEnd(true);
    }

    public List<String> searchPrefix(String prefix) {
        List<String> results = new ArrayList<>();
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            if (!node.getChildren().containsKey(c)) return results;
            node = node.getChildren().get(c);
        }
        collectWords(node, prefix, results);
        return results;
    }

    private void collectWords(TrieNode node, String currentPrefix, List<String> results) {
        if (node.isEnd()) results.add(currentPrefix);
        for (char c : node.getChildren().keySet()) {
            collectWords(node.getChildren().get(c), currentPrefix + c, results);
        }
    }
}
