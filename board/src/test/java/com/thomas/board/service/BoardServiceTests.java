package com.thomas.board.service;

import com.thomas.board.dto.BoardDTO;
import com.thomas.board.dto.PageRequestDTO;
import com.thomas.board.dto.PageResultDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister() {
        BoardDTO dto = BoardDTO.builder().title("Test.")
                                         .content("Test...")
                                         .writerEmail("user55@aaa.com")
                                         .build();

        Long bno = boardService.register(dto);
    }

    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = new PageRequestDTO();

        PageResultDTO<BoardDTO, Object[]> result = boardService.getList(pageRequestDTO);

        // result.getDtoList().forEach(i -> { System.out.println(i); });

        for(BoardDTO boardDTO : result.getDtoList()) {
            System.out.println(boardDTO);
        }
    }
}
