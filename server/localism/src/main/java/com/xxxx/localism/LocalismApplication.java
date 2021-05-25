package com.xxxx.localism;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.xxxx.localism.mapper")
public class LocalismApplication {
    public static void main(String[] args) {
        SpringApplication.run(LocalismApplication.class,args);
    }
}
