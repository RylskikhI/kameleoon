package com.rlsk.kameleoon.quote.controller;

import com.rlsk.kameleoon.quote.dto.QuoteDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/quotes")
@RequiredArgsConstructor
public class QuoteController {

    QuoteClient quoteClient;

    @PostMapping
    public ResponseEntity<Object> save(@Valid @RequestBody QuoteDto quoteDto) {
        log.info("Send post request /quotes");
        return quoteClient.save(quoteDto);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllQuotes() {
        log.info("Send get request /quotes/all");
        return quoteClient.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getQuoteById(@PathVariable Long id) {
        log.info("Send get request /quotes/{}", id);
        return quoteClient.findById(id);
    }

    @GetMapping("/random")
    public ResponseEntity<Object> getRandomQuote() {
        log.info("Send get request /quotes/random");
        return quoteClient.getRandomQuote();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody QuoteDto quoteDto) {
        log.info("Send patch request /quotes/{}", id);
        return quoteClient.update(id, quoteDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        log.info("Send delete request /quotes/{}", id);
        return quoteClient.deleteById(id);
    }

    @PatchMapping("/{id}/upvote")
    public ResponseEntity<Object> upvoteQuote(@PathVariable Long id) {
        log.info("Send patch request /quotes/{}/upvote", id);
        return quoteClient.upvoteQuote(id);
    }

    @PatchMapping("/{id}/downvote")
    public ResponseEntity<Object> downvoteQuote(@PathVariable Long id) {
        log.info("Send patch request /quotes/{}/downvote", id);
        return quoteClient.downvoteQuote(id);
    }

    @GetMapping("/top-ten")
    public ResponseEntity<Object> getTopTenQuotes() {
        log.info("Send get request /quotes/top-ten");
        return quoteClient.getTopTenQuotes();
    }

    @GetMapping("/worst-ten")
    public ResponseEntity<Object> getWorstTenQuotes() {
        log.info("Send get request /quotes/worst-ten");
        return quoteClient.getWorstTenQuotes();
    }
}
