package com.rlsk.kameleoon.quote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rlsk.kameleoon.quote.controller.QuoteController;
import com.rlsk.kameleoon.quote.dto.QuoteDto;
import com.rlsk.kameleoon.quote.mapper.QuoteMapper;
import com.rlsk.kameleoon.quote.model.Quote;
import com.rlsk.kameleoon.quote.service.QuoteService;
import com.rlsk.kameleoon.quote.utils.LocalDateTimeAdapter;
import com.rlsk.kameleoon.user.dto.UserDto;
import com.rlsk.kameleoon.user.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuoteController.class)
public class QuoteControllerTest {

    private Quote quote;
    private QuoteDto dto;
    private UserDto poster;
    private Gson gson;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuoteService quoteService;

    @BeforeEach
    void init() {
        poster = new UserDto(1L, "John Doe", "john@gmail.com");
        quote = new Quote(1L, "Per aspera ad astra", LocalDateTime.now(), LocalDateTime.now().plusDays(3), UserMapper.toUser(poster), new ArrayList<>());
        dto = QuoteMapper.toQuoteDto(quote);
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .serializeNulls()
                .create();
    }

    @Test
    @DisplayName("Send POST request /quotes")
    void testSaveQuote() throws Exception {
        Mockito.when(quoteService.save(Mockito.anyLong(), Mockito.any())).thenReturn(dto);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/quotes")
                        .header("id", poster.getId())
                        .content(gson.toJson(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(quoteService, Mockito.times(1)).save(Mockito.anyLong(), Mockito.any());
    }

    @Test
    @DisplayName("Send GET request /quotes/all")
    void testGetAllQuotes() throws Exception {
        Mockito.when(quoteService.findAll()).thenReturn(List.of(dto));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/quotes/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber());

        Mockito.verify(quoteService, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Send GET request /quotes/{id}")
    void testGetQuoteById() throws Exception {
        Mockito.when(quoteService.findById(quote.getId())).thenReturn(dto);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/quotes/{id}", quote.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber());

        Mockito.verify(quoteService, Mockito.times(1)).findById(quote.getId());
    }

    @Test
    @DisplayName("Send DELETE request /quotes/{id}")
    void testDeleteQuoteById() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/quotes/{id}", quote.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(quoteService, Mockito.times(1)).delete(quote.getId());
    }

    @Test
    @DisplayName("Send GET request /quotes/random")
    void testGetRandomQuote() throws Exception {
        Mockito.when(quoteService.getRandomQuote()).thenReturn(dto);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/quotes/random")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber());

        Mockito.verify(quoteService, Mockito.times(1)).getRandomQuote();
    }

    @Test
    @DisplayName("Send PATCH request /quotes/{id}")
    public void testUpdateQuote() throws Exception {
        dto = QuoteDto.builder()
                .content("Homo homini lupus est")
                .build();

        Mockito.when(quoteService.update(Mockito.anyLong(), Mockito.any())).thenReturn(dto);
        this.mockMvc.perform(MockMvcRequestBuilders
                        .patch("/quotes/{id}", quote.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(dto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("Homo homini lupus est"));

        Mockito.verify(quoteService, Mockito.times(1)).update(Mockito.anyLong(), Mockito.any());
    }

    @Test
    @DisplayName("Send PUT request /quotes/{id}/upvote")
    void testUpvoteQuote() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/quotes/{id}/upvote", quote.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(quoteService, Mockito.times(1)).upvoteQuote(quote.getId());
    }

    @Test
    @DisplayName("Send PUT request /quotes/{id}/downvote")
    void testDownvoteQuote() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/quotes/{id}/downvote", quote.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(quoteService, Mockito.times(1)).downvoteQuote(quote.getId());
    }

    @Test
    @DisplayName("Send GET request /quotes/worst-ten")
    void testGetWorstTenQuotes() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/quotes/worst-ten")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(quoteService, Mockito.times(1)).getWorstTenQuotes();
    }

    @Test
    @DisplayName("Send GET request /quotes/top-ten")
    void testGetTopTenQuotes() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/quotes/top-ten")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(quoteService, Mockito.times(1)).getTopTenQuotes();
    }
}
