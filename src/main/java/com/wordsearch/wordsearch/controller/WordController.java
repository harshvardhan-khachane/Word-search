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
}
