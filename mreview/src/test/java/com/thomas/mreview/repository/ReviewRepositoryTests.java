package com.thomas.mreview.repository;

import com.thomas.mreview.entity.Member;
import com.thomas.mreview.entity.Movie;
import com.thomas.mreview.entity.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class ReviewRepositoryTests {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void insertMovieReviews() {

        // 200 개의 리뷰 등록
        IntStream.rangeClosed(1, 200).forEach(i -> {
            // 영화 번호
            Long mno = (long)(Math.random() * 100) + 1;

            // 리뷰어 번호
            Long mid = (long)(Math.random() * 100) + 1;
            Member member = Member.builder().mid(mid).build();

            Review movieReview = Review.builder().member(member)
                                                 .movie(Movie.builder().mno(mno).build())
                                                 .grade((int)(Math.random() * 5) + 1)
                                                 .text("이 영화에 대한 느낌..." + i)
                                                 .build();

            reviewRepository.save(movieReview);
        });
    }
}
