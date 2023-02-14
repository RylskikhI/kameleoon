package com.rlsk.kameleoon.quote.dto;

import com.rlsk.kameleoon.user.dto.UserDto;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuoteDto {
    private Long id;
    private String content;
    private UserDto poster;
    private List<VoteDto> votes;
}
