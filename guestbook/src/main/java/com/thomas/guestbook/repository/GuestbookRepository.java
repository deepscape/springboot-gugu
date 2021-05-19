package com.thomas.guestbook.repository;

import com.thomas.guestbook.entity.Guestbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

// Querydsl 사용하게 되면, QuerydslPredicateExecutor 추가 상속
public interface GuestbookRepository extends JpaRepository<Guestbook, Long>, QuerydslPredicateExecutor<Guestbook> {
}