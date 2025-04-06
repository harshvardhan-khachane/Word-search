package com.wordsearch.wordsearch.model;

public class WordRankResponse {
    private String word;
    private int rank;
    private String message;

    public WordRankResponse(String word, int rank, String message) {
        this.word = word;
        this.rank = rank;
        this.message = message;
    }

    // Getters
    public String getWord() { return word; }
    public int getRank() { return rank; }
    public String getMessage() { return message; }
}

