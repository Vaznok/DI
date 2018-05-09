package com.epam.rd.vlasenko.parser;

public interface DiContainer {
    <T> T getInstance(Class clazz);
}
