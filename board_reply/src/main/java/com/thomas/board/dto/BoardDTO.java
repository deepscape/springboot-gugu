package com.thomas.board.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {

    private Long bno;
    private String title;
    private String content;
    private String writerEmail;     // 작성자 Id
    private String writerName;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private int replyCount;     // 해당 게시글의 댓글 수

}