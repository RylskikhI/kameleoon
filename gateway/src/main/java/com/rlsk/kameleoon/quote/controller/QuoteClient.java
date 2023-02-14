package com.rlsk.kameleoon.quote.controller;

import com.rlsk.kameleoon.client.BaseClient;
import com.rlsk.kameleoon.quote.dto.QuoteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
public class QuoteClient extends BaseClient {

    private static final String API_PREFIX = "/quotes";

    @Autowired
    public QuoteClient(@Value("${kameleoon.server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> save(QuoteDto quoteDto) {
        return post("", quoteDto);
    }

    public ResponseEntity<Object> findAll() {
        return get("");
    }

    public ResponseEntity<Object> findById(Long id) {
        return get("/" + id);
    }

    public ResponseEntity<Object> getRandomQuote() {
        return get("/random");
    }

    public ResponseEntity<Object> update(Long id, QuoteDto quoteDto) {
        return patch("/" + id, quoteDto);
    }

    public ResponseEntity<Object> deleteById(Long id) {
        return delete("/" + id);
    }

    public ResponseEntity<Object> upvoteQuote(Long id) {
        return patch("/" + id + "/upvote", id);
    }

    public ResponseEntity<Object> downvoteQuote(Long id) {
        return patch("/" + id + "/downvote", id);
    }

    public ResponseEntity<Object> getTopTenQuotes() {
        return get("/top-ten");
    }

    public ResponseEntity<Object> getWorstTenQuotes() {
        return get("/worst-ten");
    }
}
