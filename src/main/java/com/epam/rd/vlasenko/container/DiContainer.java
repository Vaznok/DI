package com.epam.rd.vlasenko.container;

public interface DiContainer {
    <T> T getInstance(Class clazz);

    <T> T getInstance(Class clazz, String beanId);
}
