package com.epam.rd.vlasenko.container;

import com.epam.rd.vlasenko.bean.BeanDefinition;
import com.epam.rd.vlasenko.bean.ConstructorParam;
import com.epam.rd.vlasenko.bean.ConstructorParamType;
import com.epam.rd.vlasenko.exception.InvalidIniConfigurationException;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;

import java.io.File;
import java.util.*;

public class IniDiContainer implements DiContainer {
    private final File config;
    private DependencyBuilder dependencyBuilder;
    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    public IniDiContainer(String config) throws ConfigurationException {
        if(!config.endsWith(".ini")) {
            throw new InvalidIniConfigurationException("Invalid file extension. Expected '.ini'");
        }
        this.config = (new File("src/main/resources/" + config));
        this.dependencyBuilder = createDependencyBuilder();
    }

    @Override
    public <T> T getInstance(Class clazz) {
        /*Set<BeanDefinition> beanDefinitionSet = beansDefinitions.get(clazz);
        if(beanDefinitionSet.size() > 1) {
            throw new IllegalArgumentException(String.format("There are %d bean definitions for %s. " +
                    "Choose one manually.", beanDefinitionSet.size(), clazz.getName()));
        } else {
            return getInstance(clazz, beanDefinitionSet.iterator().next().getId());
        }*/
        return null;
    }

    @Override
    public <T> T getInstance(Class<T> clazz, String beanId) {
        Class<?> beanDefinitionClass = beanDefinitionMap.get(beanId).getClazz();
        if(beanDefinitionClass.getName().equals(clazz.getName())) {
            return clazz.cast(dependencyBuilder.createEntity(beanId));
        } else {
            throw new IllegalArgumentException(String.format("Incorrect class '%s' for beanId '%s'", clazz, beanId));
        }
    }

    private DependencyBuilder createDependencyBuilder() throws ConfigurationException {
        HierarchicalINIConfiguration confObj = new HierarchicalINIConfiguration(config);

        Set sections = confObj.getSections();
        for (Object section : sections) {
            String sectionName = section.toString();

            String[] splitedSectionName = sectionName.split(":");
            String className = splitedSectionName[0].trim();
            String beanId = splitedSectionName[1].trim();

            Class<?> clazz = castClass(className);

            if(beanDefinitionMap.containsKey(beanId)) {
                throw new InvalidIniConfigurationException(String.format("beanId '%s' in context must be unique", beanId));
            }
            Set<ConstructorParam> constructorsList = createConstructorsList(confObj, sectionName);
            BeanDefinition beanDefinition = new BeanDefinition(clazz, beanId, constructorsList);
            beanDefinitionMap.put(beanId, beanDefinition);
        }
        return new DependencyBuilder(beanDefinitionMap);
    }

    private Set<ConstructorParam> createConstructorsList(HierarchicalINIConfiguration confObj, String sectionName) {
        Set<ConstructorParam> constructorsSet = new HashSet<>();

        SubnodeConfiguration sectionBody = confObj.getSection(sectionName);
        Iterator iterator = sectionBody.getKeys();
        while (iterator.hasNext()) {
            String keyName = iterator.next().toString();
            String valueName = sectionBody.getString(keyName);

            String[] splitedValueName = valueName.split(":");
            ConstructorParamType type = ConstructorParamType.fromString(splitedValueName[0]);

            ConstructorParam constructorParam = new ConstructorParam(keyName, splitedValueName[1], type);
            if(constructorsSet.contains(constructorParam)) {
                throw new InvalidIniConfigurationException(String.format("constructorId '%s' for bean definition must be unique", keyName));
            } else {
                constructorsSet.add(constructorParam);
            }
        }
        return constructorsSet;
    }

    private Class<?> castClass(String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new InvalidIniConfigurationException(String.format("Class '%s' not found", className), e);
        }
        return clazz;
    }
}
