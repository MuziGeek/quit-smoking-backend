package com.muzi.quitsmoking;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
@MapperScan("com.muzi.quitsmoking.mapper")
public class QuitsmokingApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuitsmokingApplication.class, args);
    }


}
