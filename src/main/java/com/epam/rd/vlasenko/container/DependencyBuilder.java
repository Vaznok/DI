package com.epam.rd.vlasenko.container;

import com.epam.rd.vlasenko.bean.BeanDefinition;
import com.epam.rd.vlasenko.sorting.DfsTopologicalSorting;
import com.epam.rd.vlasenko.sorting.TopologicalSorting;

import java.util.ArrayDeque;
import java.util.Map;

class DependencyBuilder {
    private Map<String, BeanDefinition> beanDefinitionMap;
    private TopologicalSorting sorting;

    public DependencyBuilder(Map<String, BeanDefinition> beanDefinitionMap, TopologicalSorting sorting) {
        this.beanDefinitionMap = beanDefinitionMap;
        this.sorting = sorting;
    }

    public DependencyBuilder(Map<String, BeanDefinition> beanDefinitionMap) {
        this.beanDefinitionMap = beanDefinitionMap;
        this.sorting = new DfsTopologicalSorting(beanDefinitionMap);
    }

    <T> T createBean(String beanId) {
        ArrayDeque<BeanDefinition> deque = sorting.getDependencyGraph(beanId);
        System.out.println(deque);
        return null;
    }
}
