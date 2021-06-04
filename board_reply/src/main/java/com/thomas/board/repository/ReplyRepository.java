package com.thomas.board.repository;

import com.thomas.board.entity.Board;
import com.thomas.board.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    // 게시물로 댓글 목록 가져오기
    List<Reply> getRepliesByBoardOrderByRno(Board board);

    // JPQL 을 이용해 update, delete 를 실행하기 위해서는 @Modifying 어노테이션을 같이 추가해야 한다.
    // Board 삭제시 댓글 삭제
    @Modifying
    @Query("delete from Reply r where r.board.bno = :bno")
    void deleteByBno(Long bno);

}
