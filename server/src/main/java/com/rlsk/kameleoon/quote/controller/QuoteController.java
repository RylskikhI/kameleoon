package com.rlsk.kameleoon.quote.controller;

import com.rlsk.kameleoon.quote.dto.QuoteDto;
import com.rlsk.kameleoon.quote.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/quotes")
public class QuoteController {

    private final QuoteService quoteService;

    @Autowired
    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @PostMapping
    public QuoteDto save(@RequestHeader Long id, @RequestBody QuoteDto quoteDto) {
        return quoteService.save(id, quoteDto);
    }

    @GetMapping("/all")
    public List<QuoteDto> getAllQuotes() {
        return quoteService.findAll();
    }

    @GetMapping("/{id}")
    public QuoteDto getQuoteById(@PathVariable Long id) {
        return quoteService.findById(id);
    }

    @GetMapping("/random")
    public QuoteDto getRandomQuote() {
        return quoteService.getRandomQuote();
    }

    @PatchMapping("/{id}")
    public QuoteDto update(@PathVariable Long id, @RequestBody QuoteDto quoteDto) {
        return quoteService.update(id, quoteDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        quoteService.delete(id);
    }

    @PutMapping("/{id}/upvote")
    public QuoteDto upvoteQuote(@PathVariable Long id) {
        return quoteService.upvoteQuote(id);
    }

    @PutMapping("/{id}/downvote")
    public QuoteDto downvoteQuote(@PathVariable Long id) {
        return quoteService.downvoteQuote(id);
    }

    @GetMapping("/top-ten")
    public List<QuoteDto> getTopTenQuotes() {
        return quoteService.getTopTenQuotes();
    }

    @GetMapping("/worst-ten")
    public List<QuoteDto> getWorstTenQuotes() {
        return quoteService.getWorstTenQuotes();
    }
}
