package com.epam.rd.vlasenko.sorting;

import java.util.List;

public interface TopologicalSorting<T> {
    List<T> getDependencyGraph(String beanId);
}
