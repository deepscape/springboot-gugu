package com.thomas.ex02.repository;

import com.thomas.ex02.entity.Memo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import javax.transaction.Transactional;
import java.util.List;
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

    @Test
    public void testUpdate() {
        Memo memo = Memo.builder().mno(101L).memoText("Update Text").build();
        System.out.println(memoRepository.save(memo));
    }

    @Test
    public void testDelete() {
        Long mno = 101L;
        memoRepository.deleteById(mno);
    }

    @Test
    public void testPageDefault() {
        // 1페이지 10개
        Pageable pageable = PageRequest.of(0, 10);
        Page<Memo> result = memoRepository.findAll(pageable);

        System.out.println(result);
        System.out.println("=================================");
        System.out.println("Total Pages: " + result.getTotalPages());       // 총 페이지 개수
        System.out.println("Total Count: " + result.getTotalElements());    // 전체 개수
        System.out.println("Page Number: " + result.getNumber());           // 현재 페이지 번호
        System.out.println("Page Size: " + result.getSize());               // 페이지당 데이터 개수
        System.out.println("has next page? : " + result.hasNext());         // 다음 페이지 존재 여부
        System.out.println("first page? : " + result.isFirst());             // 시작 페이지(0) 여부
        System.out.println("=================================");

        for (Memo memo : result.getContent()) {
            System.out.println(memo);
        }
    }

    @Test
    public void testSort() {
        Sort sort1 = Sort.by("mno").descending();
        Sort sort2 = Sort.by("memoText").ascending();
        Sort sortAll = sort1.and(sort2);

        Pageable pageable = PageRequest.of(0, 10, sortAll);
        Page<Memo> result = memoRepository.findAll(pageable);

        result.get().forEach(memo -> {
            System.out.println(memo);
        });
    }

    @Test
    public void testQueryMethod() {
        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(170L, 180L);

        for (Memo memo : list) {
            System.out.println(memo);
        }
    }

    @Test
    public void testQueryMethodWithPageable() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());
        Page<Memo> result = memoRepository.findByMnoBetween(110L, 120L, pageable);

        result.get().forEach(memo -> System.out.println(memo));
    }

    @Commit
    @Transactional
    @Test
    public void testDeleteQueryMethod() {
        memoRepository.deleteMemoByMnoLessThan(110L);
    }

    // JPQL Test Code
    @Test
    public void testGetListDesc() {
        List<Memo> list = memoRepository.getListDesc();
        list.forEach(memo -> System.out.println(memo));
    }
}
