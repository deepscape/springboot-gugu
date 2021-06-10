package com.thomas.mreview.service;

import com.thomas.mreview.dto.MovieDTO;
import com.thomas.mreview.dto.MovieImageDTO;
import com.thomas.mreview.entity.Movie;
import com.thomas.mreview.entity.MovieImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MovieService {

    // 영화 등록
    Long register(MovieDTO movieDTO);

    // Map 타입으로 변환
    default Map<String, Object> dtoToEntity(MovieDTO movieDTO) {
        Map<String, Object> entityMap = new HashMap<>();

        Movie movie = Movie.builder().mno(movieDTO.getMno())
                                     .title(movieDTO.getTitle())
                                     .build();

        entityMap.put("movie", movie);

        // MovieImageDTO 처리
        List<MovieImageDTO> imageDTOList = movieDTO.getImageDTOList();

        if(imageDTOList != null && imageDTOList.size() > 0) {
            List<MovieImage> movieImageList = imageDTOList.stream().map(movieImageDTO -> {
                MovieImage movieImage = MovieImage.builder().path(movieImageDTO.getPath())
                                                            .imgName(movieImageDTO.getImgName())
                                                            .uuid(movieImageDTO.getUuid())
                                                            .movie(movie)
                                                            .build();
                return movieImage;
            }).collect(Collectors.toList());

            entityMap.put("imgList", movieImageList);
        }

        return entityMap;
    }

}