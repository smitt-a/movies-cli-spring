package dev.smitt.moviesclispring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MoviesCliSpringApplication {

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(MoviesCliSpringApplication.class, args)));
    }

}
