package com.rlsk.kameleoon.quote.service;

import com.rlsk.kameleoon.exception.NoContentException;
import com.rlsk.kameleoon.quote.dto.QuoteDto;
import com.rlsk.kameleoon.quote.mapper.QuoteMapper;
import com.rlsk.kameleoon.quote.model.Quote;
import com.rlsk.kameleoon.quote.model.Vote;
import com.rlsk.kameleoon.quote.repository.QuoteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static java.util.stream.Collectors.toList;

@Service
public class QuoteServiceImpl implements QuoteService {
    private final QuoteRepository quoteRepository;

    @Autowired
    public QuoteServiceImpl(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    @Override
    @Transactional
    public QuoteDto save(Long id, QuoteDto quoteDto) {
        final Quote quote = QuoteMapper.toQuote(quoteDto);
        final Quote quoteToSave = quoteRepository.save(quote);
        return QuoteMapper.toQuoteDto(quoteToSave);
    }

    @Override
    public List<QuoteDto> findAll() {
        return quoteRepository.findAll().stream()
                .map(QuoteMapper::toQuoteDto)
                .collect(toList());
    }

    @Override
    public QuoteDto findById(Long id) {
        final Quote quote = quoteRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Quote with id=%d not found!", id))
        );
        return QuoteMapper.toQuoteDto(quote);
    }

    @Override
    public QuoteDto getRandomQuote() {
        List<Quote> quotes = quoteRepository.findAll();
        if (quotes.isEmpty()) {
            throw new NoContentException("No quotes found in the database");
        }
        Random random = new Random();
        int randomIndex = random.nextInt(quotes.size());
        return QuoteMapper.toQuoteDto(quotes.get(randomIndex));
    }

    @Override
    @Transactional
    public QuoteDto update(Long id, QuoteDto quoteDto) {
        final Quote quote = QuoteMapper.toQuote(quoteDto);
        final Quote quoteWrap = quoteRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Quote with id=%d not found!", id))
        );
        Optional.ofNullable(quote.getContent()).ifPresent(it -> {
            if (!quote.getContent().isBlank()) quoteWrap.setContent(quote.getContent());
        });
        Optional.ofNullable(quote.getDateUpdated()).ifPresent(it -> {
            if (!quote.getDateUpdated().isAfter(quote.getDateCreated())) quoteWrap.setDateUpdated(quote.getDateUpdated());
        });
        return QuoteMapper.toQuoteDto(quoteWrap);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        final Quote quoteToDelete = quoteRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Quote with id=%d not found!", id))
        );
        quoteRepository.deleteById(quoteToDelete.getId());
    }

    public QuoteDto upvoteQuote(Long id) {
        Quote quote = quoteRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Quote with id=%d not found!", id))
        );
        Vote vote = Vote.builder()
                .quote(quote)
                .value(1)
                .dateCreated(LocalDateTime.now())
                .build();
        quote.getVotes().add(vote);
        quoteRepository.save(quote);
        return QuoteMapper.toQuoteDto(quote);
    }

    public QuoteDto downvoteQuote(Long id) {
        Quote quote = quoteRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Quote with id=%d not found!", id))
        );
        Vote vote = Vote.builder()
                .quote(quote)
                .value(-1)
                .dateCreated(LocalDateTime.now())
                .build();
        quote.getVotes().add(vote);
        quoteRepository.save(quote);
        return QuoteMapper.toQuoteDto(quote);
    }

    public List<QuoteDto> getTopTenQuotes() {
        return quoteRepository.findTop10ByOrderByVotesDesc()
                .stream()
                .map(QuoteMapper::toQuoteDto)
                .collect(toList());
    }

    public List<QuoteDto> getWorstTenQuotes() {
        return quoteRepository.findTop10ByOrderByVotesAsc()
                .stream()
                .map(QuoteMapper::toQuoteDto)
                .collect(toList());
    }
}
