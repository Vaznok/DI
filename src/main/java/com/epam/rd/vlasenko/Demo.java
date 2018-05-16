package com.epam.rd.vlasenko;

import com.epam.rd.vlasenko.example.MovieLister;
import com.epam.rd.vlasenko.container.IniDiContainer;
import com.epam.rd.vlasenko.example.Supervisor;
import org.apache.commons.configuration.ConfigurationException;

public class Demo {
    public static void main(String[] args) throws ConfigurationException {

        IniDiContainer container = new IniDiContainer("context.ini");
        MovieLister mf = container.getInstance(Supervisor.class, "supervisor");

    }
}
