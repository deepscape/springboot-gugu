package com.thomas.mreview.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "movie")        // 연관 관계 주의
public class MovieImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inum;

    private String uuid;
    private String imgName;
    private String path;        // 년/월/일 폴더 구조

    /*
        Movie 와 MovieImage 를 자주 함께 사용   <- Eager Loading    <- JOIN 관계 연관 테이블 정보 모두 즉시 로딩
        Movie 와 MovieImage 를 가끔 함께 사용   <- Lazy Loading     <- 필요한 시점에, 1번 더 쿼리 실행
        이론적으로는 위와 같지만,
        실무에서는 모두 Lazy 를 쓴다. Eager Loading 사용하지 말자.
        JPQL fetch join , Entity Graph 기능으로 해결하자.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;
}
