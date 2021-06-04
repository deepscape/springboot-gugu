package com.thomas.board.repository;

import com.thomas.board.entity.Board;
import com.thomas.board.entity.Reply;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void insertReply() {

        IntStream.rangeClosed(1,300).forEach(i -> {
            // 1~100 까지 임의의 번호
            Long bno = (long)(Math.random() * 100) + 1;
            Board board = Board.builder().bno(bno).build();

            Reply reply = Reply.builder().text("Reply......" + i)
                                         .board(board)
                                         .replyer("guest")
                                         .build();

            replyRepository.save(reply);
        });
    }

    // reply, board, member table 전체를 모두 아우터 조인으로 처리        <- 비효율적
    @Test
    public void readReply1() {
        Optional<Reply> result = replyRepository.findById(1L);

        Reply reply = result.get();

        System.out.println(reply);
        System.out.println(reply.getBoard());
    }

    @Test
    public void testListByBoard() {
        List<Reply> replyList = replyRepository.getRepliesByBoardOrderByRno(Board.builder().bno(97L).build());
        replyList.forEach(reply -> System.out.println(reply));
    }
}