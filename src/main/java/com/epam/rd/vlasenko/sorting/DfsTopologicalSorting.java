package com.epam.rd.vlasenko.sorting;

import com.epam.rd.vlasenko.bean.BeanDefinition;
import com.epam.rd.vlasenko.bean.ConstructorParam;

import java.util.*;

import static com.epam.rd.vlasenko.bean.ConstructorParamType.REF;

public class DfsTopologicalSorting implements TopologicalSorting<BeanDefinition> {
    private final Map<String, BeanDefinition> beanDefinitionMap;
    private Set<BeanDefinition> visited;
    private Map<String, BeanDefinition> beanDefinitionQueue;

    public DfsTopologicalSorting(Map<String, BeanDefinition> beanDefinitionMap) {
        this.beanDefinitionMap = beanDefinitionMap;
        this.visited = new HashSet<>();
        this.beanDefinitionQueue = new HashMap<>();
    }

    @Override
    public Map<String, BeanDefinition> getDependencyGraph(String beanId) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanId);
        buildGraph(beanDefinition);
        return beanDefinitionQueue;
    }

    private void buildGraph(BeanDefinition beanDefinition) {
        if (!visited.contains(beanDefinition)) {
            visited.add(beanDefinition);
            buildReferences(beanDefinition);
        }
    }

    private void buildReferences(BeanDefinition beanDefinition) {
        Set<ConstructorParam> constructorParams = beanDefinition.getConstructorParams();
        for (ConstructorParam param : constructorParams) {
            if (param.getType().equals(REF)) {
                buildGraph(beanDefinitionMap.get(param.getValue()));
            }
        }
        beanDefinitionQueue.put(beanDefinition.getId(), beanDefinition);
    }
}
