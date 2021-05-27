package com.thomas.board.service;

import com.thomas.board.dto.BoardDTO;
import com.thomas.board.dto.PageRequestDTO;
import com.thomas.board.dto.PageResultDTO;
import com.thomas.board.entity.Board;
import com.thomas.board.entity.Member;

public interface BoardService {

    // 게시물 등록
    Long register(BoardDTO dto);

    // 게시물 목록 보기 : PageRequestDTO -> Entity -> PageResultDTO
    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);

    // DTO -> Entity
    default Board dtoToEntity(BoardDTO dto) {

        // DTO 가 연관관계를 가진 Board 엔티티와 Member 엔티티를 구성해야 한다.
        // 내부적으로 Member 엔티티를 처리하는 과정을 거쳐야 한다.
        Member member = Member.builder().email(dto.getWriterEmail()).build();

        Board board = Board.builder().bno(dto.getBno())
                                     .title(dto.getTitle())
                                     .content(dto.getContent())
                                     .writer(member)
                                     .build();

        return board;
    }

    // Entity -> DTO
    default BoardDTO entityToDTO(Board board, Member member, Long replyCount) {

        BoardDTO boardDTO = BoardDTO.builder().bno(board.getBno())
                                              .title(board.getTitle())
                                              .content(board.getContent())
                                              .regDate(board.getRegDate())
                                              .modDate(board.getModDate())
                                              .writerEmail(member.getEmail())
                                              .writerName(member.getName())
                                              .replyCount(replyCount.intValue())
                                              .build();

        return boardDTO;
    }

}
