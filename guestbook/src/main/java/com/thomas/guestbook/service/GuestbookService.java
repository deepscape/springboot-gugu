package com.thomas.guestbook.service;

import com.thomas.guestbook.dto.GuestbookDTO;
import com.thomas.guestbook.entity.Guestbook;

public interface GuestbookService {

    // 방명록 등록
    Long register(GuestbookDTO dto);

    default Guestbook dtoToEntity(GuestbookDTO dto) {
        Guestbook entity = Guestbook.builder().gno(dto.getGno())
                                              .title(dto.getTitle())
                                              .content(dto.getContent())
                                              .writer(dto.getWriter())
                                              .build();
        return entity;
    }
}
