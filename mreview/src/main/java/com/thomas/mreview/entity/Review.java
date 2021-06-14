package com.thomas.mreview.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"movie", "member"})        // toString() 호출 시에 다른 엔티티를 사용하지 않도록
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewnum;

    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private int grade;
    private String text;

    // 리뷰 평점과 내용은 수정 대상
    public void changeGrade(int grade) {
        this.grade = grade;
    }

    public void changeText(String text) {
        this.text = text;
    }

}