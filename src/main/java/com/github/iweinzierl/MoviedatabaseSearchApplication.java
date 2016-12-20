package com.github.iweinzierl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.github.iweinzierl", "de.inselhome.moviesearch"})
public class MoviedatabaseSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoviedatabaseSearchApplication.class, args);
    }
}
