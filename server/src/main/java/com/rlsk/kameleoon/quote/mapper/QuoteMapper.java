package com.rlsk.kameleoon.quote.mapper;

import com.rlsk.kameleoon.quote.dto.QuoteDto;
import com.rlsk.kameleoon.quote.model.Quote;
import com.rlsk.kameleoon.quote.model.Vote;
import com.rlsk.kameleoon.user.mapper.UserMapper;
import com.rlsk.kameleoon.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class QuoteMapper {

    public static QuoteDto toQuoteDto(Quote quote) {
        return QuoteDto.builder()
                .id(quote.getId())
                .content(quote.getContent())
                .poster(quote.getPoster())
                .votes(quote.getVotes())
                .build();
    }

    public static Quote toQuote(QuoteDto quoteDto, List<Vote> votes, User poster) {
        return new Quote(
                quoteDto.getId(),
                quoteDto.getContent(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                poster,
                votes
        );
    }

    public static Quote toQuote(QuoteDto quoteDto) {
        return Quote.builder()
                .id(quoteDto.getId())
                .content(quoteDto.getContent())
                .dateCreated(LocalDateTime.now())
                .dateUpdated(LocalDateTime.now())
                .build();
    }
}
