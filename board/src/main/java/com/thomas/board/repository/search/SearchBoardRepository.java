package com.thomas.board.repository.search;

import com.thomas.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchBoardRepository {

    // Test
    Board search1();

    Page<Object[]> searchPage(String type, String keyword, Pageable pageable);

}
