package com.rlsk.kameleoon.quote;

import com.rlsk.kameleoon.quote.dto.QuoteDto;
import com.rlsk.kameleoon.quote.model.Quote;
import com.rlsk.kameleoon.quote.model.Vote;
import com.rlsk.kameleoon.quote.service.QuoteService;
import com.rlsk.kameleoon.user.dto.UserDto;
import com.rlsk.kameleoon.user.mapper.UserMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class QuoteDbTest {

    private UserDto poster;
    private Quote quote;
    private final QuoteService quoteService;
    private final EntityManager em;

    @BeforeEach
    void init() {
        poster = new UserDto(1L, "Jack", "jack@gmail.com");

        quote = Quote.builder()
                .id(1L)
                .content("Quote 1")
                .dateCreated(LocalDateTime.now())
                .poster(UserMapper.toUser(poster))
                .votes(List.of(Vote.builder().value(1).build()))
                .build();
    }

    @Test
    void testSaveQuote() {
        QuoteDto dto = makeQuoteDto(poster);
        quoteService.save(poster.getId(), dto);

        TypedQuery<Quote> query = em.createQuery("select q from Quote as q where q.content = :content", Quote.class);
        quote = query.setParameter("content", dto.getContent()).getSingleResult();

        assertThat(quote.getContent()).isEqualTo(dto.getContent());

    }

    private QuoteDto makeQuoteDto(UserDto poster) {
        QuoteDto quoteDto = new QuoteDto();
        quoteDto.setContent("QuoteDb Test");
        quoteDto.setPoster(UserMapper.toUser(poster));
        quoteDto.setVotes(new ArrayList<>());

        return quoteDto;
    }
}
