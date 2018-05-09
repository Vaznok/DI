package com.epam.rd.vlasenko;

import com.epam.rd.vlasenko.example.MovieLister;
import com.epam.rd.vlasenko.parser.IniDiContainer;

public class Demo {
    public static void main(String[] args) {
        IniDiContainer container = new IniDiContainer("context.ini");
        MovieLister mf = container.getInstance(MovieLister.class);

    }
}
