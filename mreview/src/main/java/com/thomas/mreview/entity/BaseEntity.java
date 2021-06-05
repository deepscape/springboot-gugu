package com.thomas.mreview.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass   // 테이블 생성 x , 실제 테이블은 상속 클래스로 DB Table 이 생성된다.
// JPA 내부 Persistence Context 에서 Entity 객체가 생성/변경되는 것을 감지하는 Listener
// AuditingEntityListener 를 활성화 하려면, @EnableJpaAuditing 설정 추가 필요함
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
public class BaseEntity {

    @CreatedDate    // 생성 시간 처리
    @Column(name = "regdate", updatable = false)    // false : DB 에 반영할 때, regdate 컬럼값은 변경 x
    private LocalDateTime regDate;

    @LastModifiedDate   // 수정 시간 자동 처리
    @Column(name = "moddate")
    private LocalDateTime modDate;
}