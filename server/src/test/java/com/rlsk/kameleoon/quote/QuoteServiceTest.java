package com.rlsk.kameleoon.quote;

import com.rlsk.kameleoon.exception.NoContentException;
import com.rlsk.kameleoon.quote.dto.QuoteDto;
import com.rlsk.kameleoon.quote.mapper.QuoteMapper;
import com.rlsk.kameleoon.quote.model.Quote;
import com.rlsk.kameleoon.quote.repository.QuoteRepository;
import com.rlsk.kameleoon.quote.service.QuoteServiceImpl;
import com.rlsk.kameleoon.user.dto.UserDto;
import com.rlsk.kameleoon.user.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuoteServiceTest {

    private Quote quote;

    private UserDto poster;

    @Mock
    private QuoteRepository quoteRepository;

    @InjectMocks
    private QuoteServiceImpl quoteService;

    @BeforeEach
    void init() {
        poster = new UserDto(1L, "John Doe", "john@gmail.com");
        quote = new Quote(1L, "Per aspera ad astra", LocalDateTime.now(), LocalDateTime.now().plusDays(3),
                UserMapper.toUser(poster), new ArrayList<>());
    }

    @Test
    void testSaveQuote() {
        when(quoteRepository.save(Mockito.any())).thenReturn(quote);

        QuoteDto dto = QuoteMapper.toQuoteDto(quote);
        QuoteDto quoteToSave = quoteService.save(1L, dto);

        assertEquals(dto.getId(), quoteToSave.getId());
        assertEquals(dto.getContent(), quoteToSave.getContent());
        assertEquals(dto.getPoster(), quoteToSave.getPoster());

        verify(quoteRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void testFindAllQuotes() {
        when(quoteRepository.findAll()).thenReturn(List.of(quote));

        List<QuoteDto> quotes = quoteService.findAll();

        assertEquals(1, quotes.size());

        verify(quoteRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testFindQuoteById() {
        when(quoteRepository.findById(quote.getId())).thenReturn(Optional.of(quote));

        QuoteDto dto = quoteService.findById(quote.getId());

        assertEquals(quote.getId(), dto.getId());
        assertEquals(quote.getContent(), dto.getContent());
        assertEquals(quote.getPoster(), dto.getPoster());

        verify(quoteRepository, Mockito.times(1)).findById(quote.getId());
    }

    @ParameterizedTest
    @ValueSource(longs = {11, 12, 32, 999})
    public void testFindQuoteById_whenQuoteNotExists_thenThrowEntityNotFoundException(Long id) {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            quoteService.findById(id);
        });

        String expectedMessage = String.format("Quote with id=%d not found!", id);
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testRandomQuote_thenReturnRandomQuote() {
        Quote quote1 = new Quote(1L, "Tell me and I forget. Teach me and I remember. Involve me and I learn.", LocalDateTime.now(), LocalDateTime.now().plusDays(3),
                UserMapper.toUser(poster), new ArrayList<>());

        List<Quote> quotes = List.of(quote1);
        Mockito.when(quoteRepository.findAll()).thenReturn(quotes);

        QuoteDto randomQuote = quoteService.getRandomQuote();

        assertEquals(quotes.get(0).getContent(), QuoteMapper.toQuote(randomQuote).getContent());

        Mockito.verify(quoteRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testRandomQuote_whenNoQuotes_thenThrowNoContentException() {
        NoContentException exception = assertThrows(NoContentException.class, () -> {
            quoteService.getRandomQuote();
        });

        String expectedMessage = "No quotes found in the database";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testUpdateQuote() {
        Quote newQuote = new Quote(quote.getId(), "Hello, World!", LocalDateTime.now(), LocalDateTime.now().plusDays(3),
                UserMapper.toUser(poster), new ArrayList<>());
        Mockito.when(quoteRepository.findById(quote.getId())).thenReturn(Optional.of(quote));

        QuoteDto dto = QuoteMapper.toQuoteDto(newQuote);
        QuoteDto savedQuote = quoteService.update(newQuote.getId(), dto);

        assertEquals(newQuote.getId(), savedQuote.getId());
        assertEquals(newQuote.getContent(), savedQuote.getContent());
        assertEquals(newQuote.getPoster().getId(), savedQuote.getPoster().getId());

        Mockito.verify(quoteRepository, Mockito.times(1)).findById(quote.getId());
    }

    @ParameterizedTest
    @ValueSource(longs = {11, 12, 32, 999})
    void testUpdateQuote_whenQuoteNotExists_thenThrowEntityNotFoundException(Long id) {
        Quote newQuote = new Quote(quote.getId(), "Hello, World!", LocalDateTime.now(), LocalDateTime.now().plusDays(3),
                UserMapper.toUser(poster), new ArrayList<>());
        QuoteDto dto = QuoteMapper.toQuoteDto(newQuote);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            quoteService.update(id, dto);
        });

        String expectedMessage = String.format("Quote with id=%d not found!", id);
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testDeleteQuote() {
        Mockito.when(quoteRepository.findById(quote.getId())).thenReturn(Optional.of(quote));

        quoteService.delete(quote.getId());

        Mockito.verify(quoteRepository, Mockito.times(1)).findById(quote.getId());
    }

    @ParameterizedTest
    @ValueSource(longs = {11, 12, 32, 999})
    public void testDeleteQuote_whenQuoteNotExists_thenThrowEntityNotFoundException(Long id) {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            quoteService.delete(id);
        });

        String expectedMessage = String.format("Quote with id=%d not found!", id);
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @ParameterizedTest
    @ValueSource(longs = {11, 12, 32, 999})
    void testUpvoteQuote(Long id) {
        when(quoteRepository.findById(id)).thenReturn(Optional.of(quote));

        QuoteDto quoteDto = quoteService.upvoteQuote(id);

        assertNotNull(quoteDto);
        assertEquals(1, quote.getVotes().size());
        assertEquals(1, quote.getVotes().get(0).getValue());
        verify(quoteRepository).save(quote);
    }

    @ParameterizedTest
    @ValueSource(longs = {11, 12, 32, 999})
    public void testUpvoteQuote_whenQuoteNotExists_thenThrowEntityNotFoundException(Long id) {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            quoteService.upvoteQuote(id);
        });

        String expectedMessage = String.format("Quote with id=%d not found!", id);
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @ParameterizedTest
    @ValueSource(longs = {11, 12, 32, 999})
    void testDownvoteQuote(Long id) {
        when(quoteRepository.findById(id)).thenReturn(Optional.of(quote));

        QuoteDto quoteDto = quoteService.downvoteQuote(id);

        assertNotNull(quoteDto);
        assertEquals(1, quote.getVotes().size());
        assertEquals(-1, quote.getVotes().get(0).getValue());
        verify(quoteRepository).save(quote);
    }

    @ParameterizedTest
    @ValueSource(longs = {11, 12, 32, 999})
    public void testDownvoteQuote_whenQuoteNotExists_thenThrowEntityNotFoundException(Long id) {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            quoteService.downvoteQuote(id);
        });

        String expectedMessage = String.format("Quote with id=%d not found!", id);
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetTopTenQuotes() {
        when(quoteRepository.findTop10ByOrderByVotesDesc()).thenReturn(Arrays.asList(
                new Quote(1L, "Content 1", LocalDateTime.now(), LocalDateTime.now(), null, Collections.emptyList()),
                new Quote(2L, "Content 2", LocalDateTime.now(), LocalDateTime.now(), null, Collections.emptyList()),
                new Quote(3L, "Content 3", LocalDateTime.now(), LocalDateTime.now(), null, Collections.emptyList()),
                new Quote(4L, "Content 4", LocalDateTime.now(), LocalDateTime.now(), null, Collections.emptyList()),
                new Quote(5L, "Content 5", LocalDateTime.now(), LocalDateTime.now(), null, Collections.emptyList()),
                new Quote(6L, "Content 6", LocalDateTime.now(), LocalDateTime.now(), null, Collections.emptyList()),
                new Quote(7L, "Content 7", LocalDateTime.now(), LocalDateTime.now(), null, Collections.emptyList()),
                new Quote(8L, "Content 8", LocalDateTime.now(), LocalDateTime.now(), null, Collections.emptyList()),
                new Quote(9L, "Content 9", LocalDateTime.now(), LocalDateTime.now(), null, Collections.emptyList()),
                new Quote(10L, "Content 10", LocalDateTime.now(), LocalDateTime.now(), null, Collections.emptyList())
        ));

        List<QuoteDto> topTenQuotes = quoteService.getTopTenQuotes();

        assertEquals(10, topTenQuotes.size());
        for (int i = 0; i < 10; i++) {
            assertEquals(i + 1, topTenQuotes.get(i).getId());
            assertEquals("Content " + (i + 1), topTenQuotes.get(i).getContent());
        }
    }

    @Test
    public void testGetWorstTenQuotes() {
        when(quoteRepository.findTop10ByOrderByVotesAsc()).thenReturn(Arrays.asList(
                new Quote(1L, "Content 1", LocalDateTime.now(), LocalDateTime.now(), null, Collections.emptyList()),
                new Quote(2L, "Content 2", LocalDateTime.now(), LocalDateTime.now(), null, Collections.emptyList()),
                new Quote(3L, "Content 3", LocalDateTime.now(), LocalDateTime.now(), null, Collections.emptyList()),
                new Quote(4L, "Content 4", LocalDateTime.now(), LocalDateTime.now(), null, Collections.emptyList()),
                new Quote(5L, "Content 5", LocalDateTime.now(), LocalDateTime.now(), null, Collections.emptyList()),
                new Quote(6L, "Content 6", LocalDateTime.now(), LocalDateTime.now(), null, Collections.emptyList()),
                new Quote(7L, "Content 7", LocalDateTime.now(), LocalDateTime.now(), null, Collections.emptyList()),
                new Quote(8L, "Content 8", LocalDateTime.now(), LocalDateTime.now(), null, Collections.emptyList()),
                new Quote(9L, "Content 9", LocalDateTime.now(), LocalDateTime.now(), null, Collections.emptyList()),
                new Quote(10L, "Content 10", LocalDateTime.now(), LocalDateTime.now(), null, Collections.emptyList())
        ));

        List<QuoteDto> worstTenQuotes = quoteService.getWorstTenQuotes();

        assertEquals(10, worstTenQuotes.size());
        for (int i = 0; i < 10; i++) {
            assertEquals(i + 1, worstTenQuotes.get(i).getId());
            assertEquals("Content " + (i + 1), worstTenQuotes.get(i).getContent());
        }
    }
}
