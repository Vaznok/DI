package com.epam.rd.vlasenko.builder;

import com.epam.rd.vlasenko.parser.DiContainer;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class DependencyBuilder implements DiContainer {
    private final Map<Class, Set<String>> beansDefinitionMap;
    private final Map<String, List<String>> constructorsDefinitionMap;

    public DependencyBuilder(Map<Class, Set<String>> beansDefinitionMap,
                             Map<String, List<String>> constructorsDefinitionMap) {
        this.beansDefinitionMap = beansDefinitionMap;
        this.constructorsDefinitionMap = constructorsDefinitionMap;
    }

    @Override
    public <T> T getInstance(Class clazz) {
        for (String beanName : beansDefinitionMap.get(clazz)) {
            createBean(clazz, beanName);
        }
        return null;
    }

    private <T> T createBean(Class clazz, String bean) {

        return null;
    }

    private void createConstructors() {
        
    }
}
