package com.epam.rd.vlasenko.sorting;

import java.util.ArrayDeque;

public interface TopologicalSorting<T> {
    ArrayDeque<T> getDependencyGraph(String beanId);
}
