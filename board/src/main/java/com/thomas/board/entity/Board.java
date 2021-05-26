package com.thomas.board.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "writer")
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;
    private String content;

    // writer 는 나중에     <- 연관 관계 설정은 Entity 생성 후에 한다는 뜻

    @ManyToOne (fetch = FetchType.LAZY)     // 명시적으로 Lazy 로딩 지정
    private Member writer;      // 연관관계 지정 - 외래키로 연결된 Entity 클래스에서 설정한다.

}