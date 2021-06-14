package com.thomas.mreview.service;

import com.thomas.mreview.dto.ReviewDTO;
import com.thomas.mreview.entity.Member;
import com.thomas.mreview.entity.Movie;
import com.thomas.mreview.entity.Review;

import java.util.List;

public interface ReviewService {

    // 영화의 모든 영화 리뷰를 가져온다.
    List<ReviewDTO> getListOfMovie(Long mno);

    // 영화 리뷰 추가
    Long register(ReviewDTO reviewDTO);

    // 특정 영화리뷰 수정
    void modify(ReviewDTO reviewDTO);

    // 영화 리뷰 삭제
    void remove(Long reviewnum);

    default Review dtoToEntity(ReviewDTO reviewDTO) {
        Review movieReview = Review.builder().reviewnum(reviewDTO.getReviewnum())
                                             .movie(Movie.builder().mno(reviewDTO.getMno()).build())
                                             .member(Member.builder().mid(reviewDTO.getMid()).build())
                                             .grade(reviewDTO.getGrade())
                                             .text(reviewDTO.getText())
                                             .build();
        return movieReview;
    }

    default ReviewDTO entityToDTO(Review movieReview) {
        ReviewDTO reviewDTO = ReviewDTO.builder().reviewnum(movieReview.getReviewnum())
                                                 .mno(movieReview.getMovie().getMno())
                                                 .mid(movieReview.getMember().getMid())
                                                 .nickname(movieReview.getMember().getNickname())
                                                 .email(movieReview.getMember().getEmail())
                                                 .grade(movieReview.getGrade())
                                                 .text(movieReview.getText())
                                                 .regDate(movieReview.getRegDate())
                                                 .modDate(movieReview.getModDate())
                                                 .build();
        return reviewDTO;
    }

}