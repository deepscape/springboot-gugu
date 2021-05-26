package com.thomas.board.repository;

import com.thomas.board.entity.Board;
import com.thomas.board.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class BoardRepositoryTests {

    @Autowired
    BoardRepository boardRepository;

    @Test
    public void insertBoard() {
        IntStream.rangeClosed(1,100).forEach(i -> {
            Member member = Member.builder().email("user" + i + "@aaa.com").build();
            Board board = Board.builder().title("Title..." + i)
                                         .content("Content..." + i)
                                         .writer(member)
                                         .build();

            boardRepository.save(board);
        });
    }

    // 내부적으로 left outer join 처리
    @Transactional      // Lazy Loading 처리 , 여러 번의 쿼리를 실행하는 방식
    @Test
    public void testRead1() {
        Optional<Board> result = boardRepository.findById(100L);
        Board board = result.get();

        System.out.println(board);
        System.out.println(board.getWriter());  // Lazy Loading 의 경우 no Session 예외 발생
    }
    
}
