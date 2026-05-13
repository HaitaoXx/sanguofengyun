package com.gxa;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.gxa.mapper")
public class AiAgentAnswerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiAgentAnswerApplication.class, args);
    }

}
