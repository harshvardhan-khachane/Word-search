package com.wordsearch.wordsearch.controller;

import com.wordsearch.wordsearch.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class WordController {
    private final WordService wordService;

    @Autowired
    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @GetMapping("/auto-complete")
    public ResponseEntity<List<String>> autoComplete(@RequestParam String prefix) {
        return ResponseEntity.ok(wordService.autoComplete(prefix));
    }

    @PostMapping("/search")
    public ResponseEntity<String> searchWord(@RequestParam String word) {
        if(wordService.search(word)){
            wordService.incrementRank(word);
            return ResponseEntity.ok("Rank Incremented for " + word);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/rank")
    public ResponseEntity<Integer> getRank(@RequestParam String word) {
        int rank = wordService.getRank(word);
        return rank != -1 ? ResponseEntity.ok(rank) : ResponseEntity.notFound().build();
    }
}
