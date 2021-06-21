package com.thomas.club.repository;

import com.thomas.club.entity.Note;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    // id 로 Note 상세 정보 조회, 작성자 정보 포함
    @EntityGraph(attributePaths = "writer", type = EntityGraph.EntityGraphType.LOAD)
    @Query("select n from Note n where n.num = :num")
    Optional<Note> getWithWriter(Long num);

    // 특정 유저가 작성한 Note 목록, 작성자 정보 포함
    @EntityGraph(attributePaths = {"writer"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select n from Note n where n.writer.email = :email")
    List<Note> getList(String email);

}