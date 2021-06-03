package com.thomas.board.service;

import com.thomas.board.dto.BoardDTO;
import com.thomas.board.dto.PageRequestDTO;
import com.thomas.board.dto.PageResultDTO;
import com.thomas.board.entity.Board;
import com.thomas.board.entity.Member;
import com.thomas.board.repository.BoardRepository;
import com.thomas.board.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

@Service
@RequiredArgsConstructor        // 초기화 되지않은 final 필드나, @NonNull 이 붙은 필드에 대해 생성자를 생성
@Log4j2
public class BoardServiceImpl implements BoardService {

    private final BoardRepository repository;   // 자동 주입 final
    private final ReplyRepository replyRepository;

    // 게시물 등록
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
        // Page<Object[]> result = repository.getBoardWithReplyCount(pageRequestDTO.getPageable(Sort.by("bno").descending()));
        Page<Object[]> result = repository.searchPage(pageRequestDTO.getType(), pageRequestDTO.getKeyword(), pageRequestDTO.getPageable(Sort.by("bno").descending()));

        return new PageResultDTO<>(result, fn);
    }

    // 게시물 조회 처리
    @Override
    public BoardDTO get(Long bno) {
        Object result = repository.getBoardByBno(bno);
        Object[] arr = (Object[])result;

        return entityToDTO((Board)arr[0], (Member)arr[1], (Long)arr[2]);
    }

    // 삭제 기능 구현, 트랜잭션 추가
    @Transactional
    @Override
    public void removeWithReplies(Long bno) {
        // 댓글부터 삭제
        replyRepository.deleteByBno(bno);

        repository.deleteById(bno);
    }

    @Transactional
    @Override
    public void modify(BoardDTO boardDTO) {
        Board board = repository.getById(boardDTO.getBno());

        if(board != null) {
            board.changeTitle(boardDTO.getTitle());
            board.changeContent(boardDTO.getContent());

            repository.save(board);
        }
    }

}