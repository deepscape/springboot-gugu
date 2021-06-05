package com.thomas.mreview.repository;

import com.thomas.mreview.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    // 영화, 영화 이미지, 평점 평균, 리뷰 수
    // max(mi) 의 경우, N+1 문제를 발생시키므로 절대 유의할 것
    @Query("select m, mi, avg(coalesce(r.grade, 0)), count(distinct r) from Movie m " +
            "left join MovieImage mi on mi.movie = m " +
            "left join Review r on r.movie = m group by m")
    Page<Object[]> getListPage(Pageable pageable);

}