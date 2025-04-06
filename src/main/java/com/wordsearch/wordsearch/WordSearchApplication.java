package com.wordsearch.wordsearch;

import com.wordsearch.wordsearch.service.WordService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.nio.file.Files;
import java.util.List;

@SpringBootApplication
public class WordSearchApplication implements CommandLineRunner {
    private final WordService wordService;

    public WordSearchApplication(WordService wordService) {
        this.wordService = wordService;
    }

    public static void main(String[] args) {
        SpringApplication.run(WordSearchApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        ClassPathResource resource = new ClassPathResource("words.txt");
        List<String> words = Files.readAllLines(resource.getFile().toPath());
        wordService.loadWords(words);
        System.out.println("Loaded " + words.size() + " words");

    }

}
