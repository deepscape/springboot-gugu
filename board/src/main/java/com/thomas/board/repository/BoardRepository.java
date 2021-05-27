package com.thomas.board.repository;

import com.thomas.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 한 개의 로우(Object) 내에 Object[]로 나옴
    // 연관 관계가 있는 엔티티 조인 처리에는 left join 뒤에 on 이 없다.
    @Query("select b, w from Board b left join b.writer w where b.bno = :bno")
    Object getBoardWithWriter(@Param("bno") Long bno);

    // 연관 관계가 있는 엔티티 조인 처리에는 left join 뒤에 on 이 있다.
    @Query("select b, r from Board b left join Reply r on r.board = b where b.bno = :bno")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);

    // 목록 화면에 필요한 데이터 (게시물, 회원, 댓글)      <- 조인 후에 Board 를 기준으로 Group By
    @Query(value = "select b, w, count(r) from Board b left join b.writer w left join Reply r on r.board = b group by b",
           countQuery = "select count(b) from Board b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable);

    // 조회 화면
    @Query("select b, w, count(r) from Board b left join b.writer w left join Reply r on r.board = b where b.bno = :bno")
    Object getBoardByBno(@Param("bno") Long bno);

}