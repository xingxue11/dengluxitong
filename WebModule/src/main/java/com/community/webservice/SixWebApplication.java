package com.community.webservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.community.userservice", "com.community.communityservice", "com.community.webservice","com.community.workflowservice"})
@MapperScan(basePackages = {"com.community.userservice.mapper", "com.community.communityservice.mapper","com.community.workflowservice.mapper"})
public class SixWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SixWebApplication.class, args);
    }
}