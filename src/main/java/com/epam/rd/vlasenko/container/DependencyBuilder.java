package com.epam.rd.vlasenko.container;

import com.epam.rd.vlasenko.bean.BeanDefinition;
import com.epam.rd.vlasenko.sorting.DfsTopologicalSorting;
import com.epam.rd.vlasenko.sorting.TopologicalSorting;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DependencyBuilder {
    private Map<String, BeanDefinition> beanDefinitionMap;
    private TopologicalSorting sorting;
    private Map<String, ?> beans = new HashMap<>();

    public DependencyBuilder(Map<String, BeanDefinition> beanDefinitionMap, TopologicalSorting sorting) {
        this.beanDefinitionMap = beanDefinitionMap;
        this.sorting = sorting;
    }

    DependencyBuilder(Map<String, BeanDefinition> beanDefinitionMap) {
        this.beanDefinitionMap = beanDefinitionMap;
        this.sorting = new DfsTopologicalSorting(beanDefinitionMap);
    }

    <T> T createBean(String beanId) {
        List<BeanDefinition> beanDefinitionQueue = sorting.getDependencyGraph(beanId);
        for(BeanDefinition beanDefinition : beanDefinitionQueue) {
            try {
                constr(beanDefinition);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void constr(BeanDefinition beanDefinition) throws NoSuchMethodException {
        Class<?> clazz = beanDefinition.getClazz();
        Constructor constructor = clazz.getConstructor(String.class);
        Parameter[] parameters = constructor.getParameters();
        String name = parameters[0].getName();

    }


}
