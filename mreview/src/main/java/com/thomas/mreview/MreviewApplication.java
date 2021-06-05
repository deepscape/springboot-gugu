package com.thomas.mreview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing          // JPA 에서 시간에 대해서 자동으로 값을 넣어주는 기능
public class MreviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(MreviewApplication.class, args);
    }

}