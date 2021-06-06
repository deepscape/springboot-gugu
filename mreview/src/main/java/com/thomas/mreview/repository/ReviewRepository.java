package com.thomas.mreview.repository;

import com.thomas.mreview.entity.Movie;
import com.thomas.mreview.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 특정 영화의 모든 리뷰와 회원 닉네임
    List<Review> findByMovie(Movie movie);
}