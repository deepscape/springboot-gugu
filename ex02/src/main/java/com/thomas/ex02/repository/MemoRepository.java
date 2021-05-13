package com.thomas.ex02.repository;

import com.thomas.ex02.entity.Memo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    // Query Method : Memo 객체의 mno 값이 170부터 180 사이의 객체를 구하고, mno 의 역순으로 정렬
    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);

    // Pageable 을 통해 정렬 조건을 조절 -> 좀 더 단순한 형태의 메소드 선언
    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);

    // Delete Query Method : 메모의 번호가 10보다 작은 데이터를 삭제
    void deleteMemoByMnoLessThan(Long num);

}