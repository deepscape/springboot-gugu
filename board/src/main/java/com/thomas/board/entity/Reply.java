package com.thomas.board.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "board")
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    private String text;
    private String replyer;

    // Board 연관 관계는 나중에     <- 연관 관계 설정은 Entity 생성 후에 한다는 뜻

    @ManyToOne
    private Board board;      // 연관관계 지정 - 외래키로 연결된 Entity 클래스에서 설정한다.
}