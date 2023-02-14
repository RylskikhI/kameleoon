package com.rlsk.kameleoon.quote;

import com.rlsk.kameleoon.quote.dto.QuoteDto;
import com.rlsk.kameleoon.quote.mapper.QuoteMapper;
import com.rlsk.kameleoon.quote.model.Quote;
import com.rlsk.kameleoon.quote.model.Vote;
import com.rlsk.kameleoon.user.dto.UserDto;
import com.rlsk.kameleoon.user.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class QuoteMapperTest {

    private static final UserDto POSTER = new UserDto(1L, "John Doe", "john@gmail.com");

    private static final Quote QUOTE = new Quote(1L, "Per aspera ad astra", LocalDateTime.now(),
            LocalDateTime.now().plusDays(3), UserMapper.toUser(POSTER), new ArrayList<>());

    private static final QuoteDto DTO = QuoteMapper.toQuoteDto(QUOTE);

    @Test
    void toQuoteDto() {
        QuoteDto dto = QuoteMapper.toQuoteDto(QUOTE);
        assertEquals(QUOTE.getId(), dto.getId());
        assertEquals(QUOTE.getPoster(), dto.getPoster());
        assertEquals(QUOTE.getContent(), dto.getContent());
    }

    @ParameterizedTest
    @MethodSource("getQuote")
    void toQuote(List<Vote> votes) {
        Quote quote = votes == null ?
                QuoteMapper.toQuote(DTO) :
                QuoteMapper.toQuote(DTO, votes, UserMapper.toUser(POSTER));

        assertEquals(DTO.getId(), quote.getId());
        assertEquals(DTO.getContent(), quote.getContent());

        if (votes == null) {
            assertNull(quote.getVotes());
        } else {
            assertEquals(0, quote.getVotes().size());
        }
    }

    private static Stream<Arguments> getQuote() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(List.of())
        );
    }
}
