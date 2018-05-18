package com.epam.rd.vlasenko.example;

public class Supervisor {
    private String name;
    private MovieMaker movieMaker;
    private MovieSeller movieSeller;

    public Supervisor(String name, MovieMaker movieMaker, MovieSeller movieSeller) {
        this.name = name;
        this.movieMaker = movieMaker;
        this.movieSeller = movieSeller;
    }
}
