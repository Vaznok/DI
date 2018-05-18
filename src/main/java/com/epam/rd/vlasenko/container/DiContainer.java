package com.epam.rd.vlasenko.container;

public interface DiContainer {
    <T> T getInstance(Class<T> clazz);

    <T> T getInstance(Class<T> clazz, String beanId);
}
