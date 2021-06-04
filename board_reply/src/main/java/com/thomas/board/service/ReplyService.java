package com.thomas.board.service;

import com.thomas.board.dto.ReplyDTO;
import com.thomas.board.entity.Board;
import com.thomas.board.entity.Reply;

import java.util.List;

public interface ReplyService {

    // 댓글 등록
    Long register(ReplyDTO replyDTO);

    // 특정 게시물의 댓글 목록
    List<ReplyDTO> getList(Long bno);

    // 댓글 수정
    void modify(ReplyDTO replyDTO);

    // 댓글 삭제
    void remove(Long rno);

    // ReplyDTO -> Entity   / Board 객체의 처리가 수반됨
    default Reply dtoToEntity(ReplyDTO replyDTO) {
        Board board = Board.builder().bno(replyDTO.getBno()).build();

        Reply reply = Reply.builder().rno(replyDTO.getRno())
                                     .text(replyDTO.getText())
                                     .replyer(replyDTO.getReplyer())
                                     .board(board)
                                     .build();
        return reply;
    }

    // Entity -> ReplyDTO   / Board 객체가 필요하지 않음, 게시물 번호만
    default ReplyDTO entityToDTO(Reply reply) {
        ReplyDTO dto = ReplyDTO.builder().rno(reply.getRno())
                                         .text(reply.getText())
                                         .replyer(reply.getReplyer())
                                         .regDate(reply.getRegDate())
                                         .modDate(reply.getModDate())
                                         .build();
        return dto;
    }
}