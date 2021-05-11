package com.thomas.ex02.repository;

import com.thomas.ex02.entity.Memo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTests {

    @Autowired
    MemoRepository memoRepository;

    // MemoRepository 가 정상적으로 스프링에서 처리되고, 의존성 주입에 문제가 없는지 확인
    @Test
    public void testClass() {
        System.out.println(memoRepository.getClass().getName());
    }

    @Test
    public void testInsertDummies() {
        IntStream.rangeClosed(1,100).forEach( i -> {
            Memo memo = Memo.builder().memoText("Sample..." + i).build();
            memoRepository.save(memo);
        });
    }

    @Test
    public void testSelect1() {
        // 데이터베이스에 존재하는 mno
        Long mno = 101L;

        Optional<Memo> result = memoRepository.findById(mno);

        System.out.println("= Anti Pattern ================================");
        if(result.isPresent()) {
            Memo memo = result.get();
            System.out.println(memo);
        }

        // http://homoefficio.github.io/2019/10/03/Java-Optional-%EB%B0%94%EB%A5%B4%EA%B2%8C-%EC%93%B0%EA%B8%B0/

        System.out.println("= Good Pattern (1) ================================");
        System.out.println(result.orElse(null));    // 새로운 객체를 생성할 필요가 없을 떄

        System.out.println("= Good Pattern (2) ================================");
        System.out.println(result.orElseGet(Memo::new));    // 새로운 객체를 생성할 필요가 있을 때

        System.out.println("= Good Pattern (3) ================================");
        System.out.println(result.orElseThrow( () -> new NoSuchElementException()));
    }

    @Transactional
    @Test
    public void testSelect2() {
        // 데이터베이스에 존재하는 mno
        Long mno = 201L;

        try {
            Memo memo = memoRepository.getOne(mno);

            System.out.println("=================================");
            System.out.println(memo);
        } catch (Exception e) {
            System.out.println("=================================");
            System.out.println("null");
        }
    }
}
