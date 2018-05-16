package com.epam.rd.vlasenko.example;

public class Supervisor {
    private String name;
    private MovieMaker movieMaker;
    private MovieMaker2 movieMaker2;

    public Supervisor(String name, MovieMaker movieMaker, MovieMaker2 movieMaker2) {
        this.name = name;
        this.movieMaker = movieMaker;
        this.movieMaker2 = movieMaker2;
    }
}
