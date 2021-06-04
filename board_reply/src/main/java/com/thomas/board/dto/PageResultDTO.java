package com.thomas.board.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<DTO, EN> {

    // DTO 리스트
    private List<DTO> dtoList;

    // 총 페이지 번호
    private int totalPage;

    // 현재 페이지, 목록 사이즈, 시작 페이지 번호, 끝 페이지 번호
    private int page, size, start, end;

    // 이전, 다음
    private boolean prev, next;

    // 페이지 번호 목록
    private List<Integer> pageList;

    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn) {

        // Page 타입의 result  ->  EnToDTO 변환 기능을 하는 function 을 통해서 DTO 로 변환
        dtoList = result.stream().map(fn).collect(Collectors.toList());

        totalPage = result.getTotalPages();

        makePageList(result.getPageable());
    }

    // Pageable 객체에서 제공할 수 없는 항목들을 추가 정의
    private void makePageList(Pageable pageable) {
        this.page = pageable.getPageNumber() + 1;       // 0 부터 시작하므로 1 추가
        this.size = pageable.getPageSize();

        // temp end page
        int tempEnd = (int)(Math.ceil(page/10.0)) * 10;
        start = tempEnd - 9;

        prev = start > 1;
        end = totalPage > tempEnd ? tempEnd : totalPage;
        next = totalPage > tempEnd;

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }

}
