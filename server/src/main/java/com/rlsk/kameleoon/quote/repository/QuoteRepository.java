package com.rlsk.kameleoon.quote.repository;

import com.rlsk.kameleoon.quote.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuoteRepository extends JpaRepository<Quote, Long> {

        List<Quote> findTop10ByOrderByVotesDesc();

        List<Quote> findTop10ByOrderByVotesAsc();

}
