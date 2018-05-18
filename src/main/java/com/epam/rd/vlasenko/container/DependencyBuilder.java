package com.epam.rd.vlasenko.container;

import com.epam.rd.vlasenko.bean.BeanDefinition;
import com.epam.rd.vlasenko.bean.ConstructorParam;
import com.epam.rd.vlasenko.exception.BeanReflectionBuildException;
import com.epam.rd.vlasenko.exception.InconvertibleTypeException;
import com.epam.rd.vlasenko.sorting.TopologicalSorting;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;

import static com.epam.rd.vlasenko.bean.ConstructorParamType.VAL;

class DependencyBuilder {
    private TopologicalSorting sorting;
    private Map<String, Object> beans;

    DependencyBuilder(TopologicalSorting sorting) {
        this.sorting = Objects.requireNonNull(sorting);
        this.beans = new HashMap<>();
    }

    Object createEntity(String beanId) {
        if(beans.containsKey(beanId)) {
            return beans.get(beanId);
        }
        List<BeanDefinition> beanDefinitionList = sorting.getDependencyGraph(beanId);
        for(BeanDefinition beanDefinition : beanDefinitionList) {
            if(beans.containsKey(beanDefinition.getId())) {
                continue;
            }
            creationBeanManager(beanDefinition);
        }
        return beans.get(beanId);
    }

    private void creationBeanManager(BeanDefinition beanDefinition) {
        Class<?> clazz = beanDefinition.getClazz();
        Constructor[] constructors = clazz.getConstructors();
        Set<ConstructorParam> contextParams = beanDefinition.getConstructorParams();

        for(Constructor constructor : constructors) {
            if(isAppropriateConstructor(constructor, contextParams)) {
                try {
                    createBean(constructor, beanDefinition);
                } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    throw new BeanReflectionBuildException(String.format("Attempt to create bean '%s' failed", beanDefinition.getId()), e);
                }
            }
        }
    }

    private boolean isAppropriateConstructor(Constructor constructor, Set<ConstructorParam> contextParams) {
        Parameter[] reflectedParams = constructor.getParameters();

        Set<String> reflectedParamsName = new HashSet<>();
        for (Parameter parameter : reflectedParams) {
            reflectedParamsName.add(parameter.getName());
        }

        Set<String> paramsName = new HashSet<>();
        for (ConstructorParam constructorParam : contextParams) {
            paramsName.add(constructorParam.getId());
        }

        return reflectedParamsName.equals(paramsName);
    }

    private void createBean(Constructor constructor, BeanDefinition beanDefinition) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Parameter[] reflectedParams = constructor.getParameters();
        Set<ConstructorParam> contextParams = beanDefinition.getConstructorParams();

        Object[] params = new Object[reflectedParams.length];
        for (int i = 0; i < reflectedParams.length; i++) {
            String paramName = reflectedParams[i].getName();
            ConstructorParam contextParam = contextParams.stream().filter(param -> param.getId().equals(paramName)).findFirst().get();
            params[i] = parseContextParam(reflectedParams[i].getType(), contextParam);
        }
        beans.put(beanDefinition.getId(), constructor.newInstance(params));
    }

    private Object parseContextParam(Class<?> clazz, ConstructorParam contextParam) {
        if(contextParam.getType().equals(VAL)) {
            if (clazz == String.class) {
                return contextParam.getValue();
            }
            if (clazz == Integer.class) {
                return Integer.valueOf(contextParam.getValue());
            }
            if (clazz == Double.class) {
                return Double.valueOf(contextParam.getValue());
            }
            throw new InconvertibleTypeException("Can't convert type " + clazz);
        } else {
            return beans.get(contextParam.getValue());
        }
    }
}
