package com.thomas.board.service;

import com.thomas.board.dto.BoardDTO;
import com.thomas.board.dto.PageRequestDTO;
import com.thomas.board.dto.PageResultDTO;
import com.thomas.board.entity.Board;
import com.thomas.board.entity.Member;
import com.thomas.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor        // 초기화 되지않은 final 필드나, @NonNull 이 붙은 필드에 대해 생성자를 생성
@Log4j2
public class BoardServiceImpl implements BoardService {

    private final BoardRepository repository;   // 자동 주입 final

    @Override
    public Long register(BoardDTO dto) {
        log.info(dto);

        Board board = dtoToEntity(dto);

        repository.save(board);

        return board.getBno();
    }

    // 게시물 목록 보기 : PageRequestDTO -> Object[] -> PageResultDTO
    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

        log.info(pageRequestDTO);

        // 목록 화면에 필요한 데이터 (게시물, 회원, 댓글)      <- 조인 후에 Board 를 기준으로 Group By
        // Object[] -> DTO 로 변환
        Function<Object[], BoardDTO> fn = (en -> entityToDTO((Board)en[0], (Member)en[1], (Long)en[2]));

        // PageResultDTO 에 전달할 인자
        Page<Object[]> result = repository.getBoardWithReplyCount(pageRequestDTO.getPageable(Sort.by("bno").descending()));

        return new PageResultDTO<>(result, fn);
    }

}
