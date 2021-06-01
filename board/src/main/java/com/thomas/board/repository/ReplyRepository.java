package com.thomas.board.repository;

import com.thomas.board.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    // JPQL 을 이용해 update, delete 를 실행하기 위해서는 @Modifying 어노테이션을 같이 추가해야 한다.
    @Modifying
    @Query("delete from Reply r where r.board.bno = :bno")
    void deleteByBno(Long bno);
}
