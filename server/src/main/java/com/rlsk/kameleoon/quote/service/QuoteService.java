package com.rlsk.kameleoon.quote.service;

import com.rlsk.kameleoon.quote.dto.QuoteDto;

import java.util.List;

public interface QuoteService {
    QuoteDto save(Long id, QuoteDto quoteDto);

    List<QuoteDto> findAll();

    QuoteDto findById(Long id);

    QuoteDto getRandomQuote();

    QuoteDto update(Long id, QuoteDto quoteDto);

    void delete(Long id);

    QuoteDto upvoteQuote(Long id);

    QuoteDto downvoteQuote(Long id);

    List<QuoteDto> getTopTenQuotes();

    List<QuoteDto> getWorstTenQuotes();
}
