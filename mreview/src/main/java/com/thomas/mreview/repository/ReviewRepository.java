package com.thomas.mreview.repository;

import com.thomas.mreview.entity.Member;
import com.thomas.mreview.entity.Movie;
import com.thomas.mreview.entity.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 특정 영화의 모든 리뷰와 회원 닉네임
    @EntityGraph(attributePaths = {"member"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Review> findByMovie(Movie movie);

    // 회원을 삭제하려면, 먼저 review table 에서 삭제하고, m_member table 에서 삭제해야 한다.
    @Modifying
    @Query("delete from Review mr where mr.member = :member")
    void deleteByMember(Member member);
}