package com.thomas.ex03.controller;

import com.thomas.ex03.dto.SampleDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/sample")
@Log4j2
public class SampleController {

    @GetMapping("/ex1")
    public void ex1() {
        log.info("ex1................");
    }

    // {} 를 사용하면 하나 이상의 URL 을 지정할 수 있다.
    @GetMapping({"/ex2", "/exLink"})
    public void exModel(Model model) {
        List<SampleDTO> list = IntStream.rangeClosed(1,20).asLongStream().mapToObj(i -> {
            SampleDTO dto = SampleDTO.builder().sno(i)
                                               .first("First.."+i)
                                               .last("Last.."+i)
                                               .regTime(LocalDateTime.now())
                                               .build();
            return dto;
        }).collect(Collectors.toList());

        model.addAttribute("list", list);
    }

    @GetMapping({"/exInline"})
    public String exInline(RedirectAttributes redirectAttributes) {
        log.info("exInline..................");

        SampleDTO dto = SampleDTO.builder().sno(100L)
                                           .first("First..100")
                                           .last("Last..100")
                                           .regTime(LocalDateTime.now())
                                           .build();

        redirectAttributes.addFlashAttribute("result", "succcess");
        redirectAttributes.addFlashAttribute("dto", dto);

        return "redirect:/sample/ex3";
    }

    @GetMapping("/ex3")
    public void ex3() {
        log.info("ex3");
    }

    // url name 에 해당하는 html 파일과 매핑된다.
    @GetMapping({"/exLayout1", "/exLayout2", "/exTemplate", "/exSidebar"})
    public void exLayout1() {
        log.info("exLayout.................");
    }

}
