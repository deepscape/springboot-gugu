package com.thomas.guestbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.thomas.guestbook.entity.Guestbook;
import com.thomas.guestbook.entity.QGuestbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestbookRepositoryTests {

    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test
    public void insertDummies() {
        IntStream.rangeClosed(1,300).forEach(i -> {
            Guestbook guestbook = Guestbook.builder().title("Title..." + i)
                                                     .content("Content..." + i)
                                                     .writer("user" + (i % 10))
                                                     .build();
            System.out.println(guestbookRepository.save(guestbook));
        });
    }

    @Test
    public void updateTest() {
        Optional<Guestbook> result = guestbookRepository.findById(300L);

        if(result.isPresent()) {
            Guestbook guestbook = result.get();

            guestbook.changeTitle("Changed Title...");
            guestbook.changeContent("Changed Content...");

            guestbookRepository.save(guestbook);
        }
    }

    // 단일 항목 검색 테스트 : title 에 1 이라는 글자가 있는 엔티티 검색
    @Test
    public void testQuery1() {
        PageRequest pageable = PageRequest.of(0,10, Sort.by("gno").descending());

        // (1) 동적 처리를 위해 Q-도메인 클래스를 얻어온다. Q-도메인 클래스를 이용하면 엔티티 클래스에 선언된 title, content 같은 필드를 변수로 활용할 수 있다.
        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";

        // (2) BooleanBuilder 는 where 문에 들어가는 조건들을 넣어주는 컨테이너 (type Predicate)
        BooleanBuilder builder = new BooleanBuilder();

        // (3) 원하는 조건은 필드 값과 같이 결합해서 생성
        // BooleanBuilder 안에 들어가는 값은 com.querydsl.core.types.Predicate 타입이어야 한다.
        BooleanExpression expression = qGuestbook.title.contains(keyword);

        // (4) 만들어진 조건은 where 문에 and 나 or 같은 키워드와 결합시킨다.
        builder.and(expression);

        // (5) BooleanBuilder 는 GuestbookRepository 에 추가된 QuerydslPredicateExecutor 인터페이스의 findAll() 을 사용
        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }

    // 다중 항목 검색 테스트 : 제목 혹은 내용에 특정한 키워드가 있고 gno 가 250보다 크다
    // BooleanBuilder 는 and(), or() 의 파라미터로 BooleanExpression 을 전달해서 복합적인 쿼리 생성
    @Test
    public void testQuery2() {
        Pageable pageable = PageRequest.of(0,10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression exTitle = qGuestbook.title.contains(keyword);
        BooleanExpression exContent =qGuestbook.content.contains(keyword);

        // Boolean Expression 을 결합하는 부분
        BooleanExpression exAll = exTitle.or(exContent);

        // BooleanBuilder 에 BooleanExpression 을 추가
        builder.and(exAll);

        // 'gno 가 250 보다 크다' 라는 조건을 추가
        builder.and(qGuestbook.gno.gt(250L));

        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }

}