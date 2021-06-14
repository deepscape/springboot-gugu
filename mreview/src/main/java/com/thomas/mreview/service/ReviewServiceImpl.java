package com.thomas.mreview.service;

import com.thomas.mreview.dto.ReviewDTO;
import com.thomas.mreview.entity.Movie;
import com.thomas.mreview.entity.Review;
import com.thomas.mreview.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public List<ReviewDTO> getListOfMovie(Long mno) {
        Movie movie = Movie.builder().mno(mno).build();
        List<Review> result = reviewRepository.findByMovie(movie);

        // entity result 를 dto 로 변환해서 List 타입으로 리턴
        return result.stream().map(movieReview -> entityToDTO(movieReview)).collect(Collectors.toList());
    }

    @Override
    public Long register(ReviewDTO reviewDTO) {
        Review movieReview = dtoToEntity(reviewDTO);
        reviewRepository.save(movieReview);

        return movieReview.getReviewnum();
    }

    @Override
    public void modify(ReviewDTO reviewDTO) {
        Optional<Review> result = reviewRepository.findById(reviewDTO.getReviewnum());

        if(result.isPresent()) {
            Review movieReview = result.get();
            movieReview.changeGrade(reviewDTO.getGrade());
            movieReview.changeText(reviewDTO.getText());

            reviewRepository.save(movieReview);
        }
    }

    @Override
    public void remove(Long reviewnum) {
        reviewRepository.deleteById(reviewnum);
    }
}
