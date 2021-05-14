package com.thomas.ex02.repository;

import com.thomas.ex02.entity.Memo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    // Query Method : Memo 객체의 mno 값이 170부터 180 사이의 객체를 구하고, mno 의 역순으로 정렬
    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);

    // Pageable 을 통해 정렬 조건을 조절 -> 좀 더 단순한 형태의 메소드 선언
    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);

    // Delete Query Method : 메모의 번호가 10보다 작은 데이터를 삭제
    void deleteMemoByMnoLessThan(Long num);

    // @Query Annotation - JPQL : 테이블 대신 엔티티 클래스 이용
    @Query("select m from Memo m order by m.mno desc")
    List<Memo> getListDesc();

    @Transactional
    @Modifying
    @Query("update Memo m set m.memoText = :memoText where m.mno = :mno")
    int updateMemoText(@Param("mno") Long mno, @Param("memoText") String memoText);

    @Query(value = "select m from Memo m where m.mno > :mno", countQuery = "select count(m) from Memo m where m.mno > :mno")
    Page<Memo> getListWithQuery(Long mno, Pageable pageable);

    @Query(value = "select m.mno, m.memoText, CURRENT_DATE from Memo m where m.mno > :mno", countQuery = "select count(m) from Memo m where m.mno > :mno")
    Page<Object[]> getListWithQueryObject(Long mno, Pageable pageable);

    // Native SQL 처리
    @Query(value = "select * from tbl_memo where mno > 0", nativeQuery = true)
    List<Object[]> getNativeResult();

}