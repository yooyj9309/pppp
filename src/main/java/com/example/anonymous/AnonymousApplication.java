package com.example.anonymous;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class AnonymousApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnonymousApplication.class, args);
    }

}
