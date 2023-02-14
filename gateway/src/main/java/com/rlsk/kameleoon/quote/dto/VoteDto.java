package com.rlsk.kameleoon.quote.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteDto {
    private Long id;
    private Integer value;
    private LocalDateTime dateCreated;
    private QuoteDto quoteDto;
}
