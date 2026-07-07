package com.community.webservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.sixcom.userservice", "com.sixcom.webservice"})
@MapperScan(basePackages = {"com.sixcom.userservice.mapper"})
public class SixWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SixWebApplication.class, args);
    }
}