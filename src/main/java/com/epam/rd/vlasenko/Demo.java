package com.epam.rd.vlasenko;

import com.epam.rd.vlasenko.container.IniDiContainer;
import com.epam.rd.vlasenko.example.MovieMaker;
import com.epam.rd.vlasenko.example.Supervisor;
import org.apache.commons.configuration.ConfigurationException;

public class Demo {
    public static void main(String[] args) {

        IniDiContainer container = new IniDiContainer("context.ini");

        Supervisor supervisor = container.getInstance(Supervisor.class);
        MovieMaker movieMaker = container.getInstance(MovieMaker.class, "movieMaker");

    }
}
