package com.epam.rd.vlasenko.container;

import com.epam.rd.vlasenko.bean.BeanDefinition;
import com.epam.rd.vlasenko.bean.ConstructorParam;
import com.epam.rd.vlasenko.bean.ConstructorParamType;
import com.epam.rd.vlasenko.exception.AutoBeanDetectionException;
import com.epam.rd.vlasenko.exception.InvalidIniConfigurationException;
import com.epam.rd.vlasenko.sorting.DfsTopologicalSorting;
import com.epam.rd.vlasenko.sorting.TopologicalSorting;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;

import java.io.File;
import java.util.*;

public class IniDiContainer implements DiContainer {
    private File config;
    private Map<String, BeanDefinition> beanDefinitionMap;
    private TopologicalSorting sorting;
    private DependencyBuilder dependencyBuilder;

    public IniDiContainer(String config, TopologicalSorting sorting) {
        this.config = Objects.requireNonNull(createFileInstance(config));
        this.beanDefinitionMap = Objects.requireNonNull(createBeanDefinitionMap());
        this.sorting = Objects.requireNonNull(sorting);
        this.dependencyBuilder = new DependencyBuilder(sorting);
    }

    public IniDiContainer(String config) {
        this.config = Objects.requireNonNull(createFileInstance(config));
        this.beanDefinitionMap = Objects.requireNonNull(createBeanDefinitionMap());
        this.sorting = new DfsTopologicalSorting(beanDefinitionMap);
        this.dependencyBuilder = new DependencyBuilder(sorting);
    }

    @Override
    public <T> T getInstance(Class<T> clazz) {
        String beanId = getBeanIdByClass(clazz);
        return getInstance(clazz, beanId);
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

    private File createFileInstance(String fileName) {
        if(!fileName.endsWith(".ini")) {
            throw new InvalidIniConfigurationException("Invalid file extension. Expected '.ini'");
        }
        return new File("src/main/resources/" + fileName);
    }

    private Map<String, BeanDefinition> createBeanDefinitionMap() {
        Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

        HierarchicalINIConfiguration confObj;
        try {
            confObj = new HierarchicalINIConfiguration(config);
        } catch (ConfigurationException e) {
            throw new InvalidIniConfigurationException("Attempt to build HierarchicalINIConfiguration failed", e);
        }

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
            Set<ConstructorParam> constructorsList = createConstructorsSet(confObj, sectionName);
            BeanDefinition beanDefinition = new BeanDefinition(clazz, beanId, constructorsList);
            beanDefinitionMap.put(beanId, beanDefinition);
        }
        return beanDefinitionMap;
    }

    private Set<ConstructorParam> createConstructorsSet(HierarchicalINIConfiguration confObj, String sectionName) {
        Set<ConstructorParam> constructorsSet = new HashSet<>();

        SubnodeConfiguration sectionBody = confObj.getSection(sectionName);
        Iterator iterator = sectionBody.getKeys();
        while (iterator.hasNext()) {
            String keyName = iterator.next().toString();
            String valueName = sectionBody.getString(keyName);

            // TODO: 5/18/2018 change on normal regex with validation
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

    private String getBeanIdByClass(Class clazz) {
        Map<String, String> uniqueBean = new HashMap<>();
        String uniqueClassName = clazz.getName();

        HierarchicalINIConfiguration confObj;
        try {
            confObj = new HierarchicalINIConfiguration(config);
        } catch (ConfigurationException e) {
            throw new InvalidIniConfigurationException("Attempt to build HierarchicalINIConfiguration failed", e);
        }

        Set sections = confObj.getSections();
        for (Object section : sections) {
            String sectionName = section.toString();

            String[] splitedSectionName = sectionName.split(":");
            String className = splitedSectionName[0].trim();
            String beanId = splitedSectionName[1].trim();

            if(uniqueClassName.equals(className)) {
                if(!uniqueBean.containsKey(className)) {
                    uniqueBean.put(className, beanId);
                } else {
                    throw new AutoBeanDetectionException("There are some bean definitions in context. Please, indicate required beanId");
                }
            }
        }
        return uniqueBean.get(uniqueClassName);
    }
}
