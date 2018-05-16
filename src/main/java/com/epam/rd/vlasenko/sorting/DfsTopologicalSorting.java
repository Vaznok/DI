package com.epam.rd.vlasenko.sorting;

import com.epam.rd.vlasenko.bean.BeanDefinition;
import com.epam.rd.vlasenko.bean.ConstructorParam;

import java.util.*;

import static com.epam.rd.vlasenko.bean.ConstructorParamType.REF;

public class DfsTopologicalSorting implements TopologicalSorting<BeanDefinition> {
    private final Map<String, BeanDefinition> beanDefinitionMap;
    private Set<BeanDefinition> visited;
    private ArrayDeque<BeanDefinition> deque;

    public DfsTopologicalSorting(Map<String, BeanDefinition> beanDefinitionMap) {
        this.beanDefinitionMap = beanDefinitionMap;
        this.visited = new HashSet<>();
        this.deque = new ArrayDeque<>();
    }

    @Override
    public ArrayDeque<BeanDefinition> getDependencyGraph(String beanId) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanId);
        buildGraph(beanDefinition);
        return deque;
    }

    private void buildGraph(BeanDefinition beanDefinition) {
        if (!visited.contains(beanDefinition)) {
            visited.add(beanDefinition);
            searchDependency(beanDefinition);
        }
    }

    private void searchDependency(BeanDefinition beanDefinition) {
        Set<ConstructorParam> constructorParams = beanDefinition.getConstructorParams();
        for (ConstructorParam param : constructorParams) {
            if (param.getType().equals(REF)) {
                buildGraph(beanDefinitionMap.get(param.getValue()));
            }
        }
        deque.addLast(beanDefinition);
    }
}
