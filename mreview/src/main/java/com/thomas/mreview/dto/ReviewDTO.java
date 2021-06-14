package com.thomas.mreview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private Long reviewnum;

    // Movie Number
    private Long mno;

    // Member Info
    private Long mid;
    private String nickname;
    private String email;

    private int grade;
    private String text;
    private LocalDateTime regDate, modDate;

}