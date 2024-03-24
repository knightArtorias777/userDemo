package com.jia.ac;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.jia.ac.Mapper")
public class AcApplication {

    public static void main(String[] args) {
        SpringApplication.run(AcApplication.class, args);
    }

}
