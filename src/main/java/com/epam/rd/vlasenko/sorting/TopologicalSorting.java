package com.epam.rd.vlasenko.sorting;

import java.util.Map;

public interface TopologicalSorting<T> {
    Map<String, T> getDependencyGraph(String beanId);
}
