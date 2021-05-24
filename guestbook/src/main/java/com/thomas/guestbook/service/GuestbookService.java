package com.thomas.guestbook.service;

import com.thomas.guestbook.dto.GuestbookDTO;
import com.thomas.guestbook.dto.PageRequestDTO;
import com.thomas.guestbook.dto.PageResultDTO;
import com.thomas.guestbook.entity.Guestbook;

public interface GuestbookService {

    // 방명록 등록
    Long register(GuestbookDTO dto);

    // 방명록 리스트 가져오기
    PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO);

    default Guestbook dtoToEntity(GuestbookDTO dto) {
        Guestbook entity = Guestbook.builder().gno(dto.getGno())
                                              .title(dto.getTitle())
                                              .content(dto.getContent())
                                              .writer(dto.getWriter())
                                              .build();
        return entity;
    }

    default GuestbookDTO entityToDto(Guestbook entity) {
        GuestbookDTO dto = GuestbookDTO.builder().gno(entity.getGno())
                                                 .title(entity.getTitle())
                                                 .content(entity.getContent())
                                                 .writer(entity.getWriter())
                                                 .regDate(entity.getRegDate())
                                                 .modDate(entity.getModDate())
                                                 .build();

        return dto;
    }
}
