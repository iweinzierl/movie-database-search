package com.github.iweinzierl;

import de.inselhome.moviesearch.tmdb.TmdbSearchProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MoviedatabaseSearchApplication {

    @Bean
    public TmdbSearchProvider createTmdbSearchProvider() {
        return new TmdbSearchProvider("653ce9ba629b16be9a1f5b1e4faf3697");
    }

    public static void main(String[] args) {
        SpringApplication.run(MoviedatabaseSearchApplication.class, args);
    }
}
