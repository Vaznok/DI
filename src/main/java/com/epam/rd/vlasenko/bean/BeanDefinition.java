package com.epam.rd.vlasenko.bean;

import java.util.Set;

public class BeanDefinition {
    private Class<?> clazz;
    private String id;
    private Set<ConstructorParam> constructorParams;

    public BeanDefinition(Class<?> clazz, String id, Set<ConstructorParam> constructorParams) {
        this.clazz = clazz;
        this.id = id;
        this.constructorParams = constructorParams;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getId() {
        return id;
    }

    public Set<ConstructorParam> getConstructorParams() {
        return constructorParams;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BeanDefinition that = (BeanDefinition) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
