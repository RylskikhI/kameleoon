package com.rlsk.kameleoon.quote.dto;

import com.rlsk.kameleoon.quote.model.Vote;
import com.rlsk.kameleoon.user.model.User;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class QuoteDto {
    private Long id;
    private String content;
    private User poster;
    private List<Vote> votes;
}
