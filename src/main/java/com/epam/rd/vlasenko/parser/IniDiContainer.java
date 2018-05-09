package com.epam.rd.vlasenko.parser;

import com.epam.rd.vlasenko.builder.DependencyBuilder;
import com.epam.rd.vlasenko.exception.InvalidIniConfigurationException;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;

import java.io.File;
import java.util.*;

public class IniDiContainer implements DiContainer {
    private final File config;
    private DependencyBuilder dependencyBuilder;

    public IniDiContainer(String config) {
        if(!config.endsWith(".ini")) {
            throw new InvalidIniConfigurationException("Invalid file extension. Expected '.ini'");
        }
        this.config = (new File("src/main/resources/" + config));
    }

    public <T> T getInstance(Class clazz) {
        try {
            createDependencyBuilder();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        return dependencyBuilder.getInstance(clazz);
    }

    private void createDependencyBuilder() throws ConfigurationException {
        Map<Class, Set<String>> beansDefinitionMap = new HashMap<>();
        Map<String, List<String>> constructorsDefinitionMap = new HashMap<>();

        HierarchicalINIConfiguration confObj = new HierarchicalINIConfiguration(config);

        Set sections = confObj.getSections();
        for (Object section : sections) {

            String sectionName = section.toString();
            String[] splitedSectionName = sectionName.split(":");

            String className = splitedSectionName[0].trim();
            String beanName = splitedSectionName[1].trim();

            if(constructorsDefinitionMap.containsKey(beanName)) {
                throw new InvalidIniConfigurationException(String.format("Config variable '%s' for bean definition must be unique", beanName));
            }

            Class clazz;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new InvalidIniConfigurationException("Class '%s' not found", e);
            }

            if(beansDefinitionMap.containsKey(clazz)) {
                beansDefinitionMap.get(clazz).add(beanName);
            } else {
                Set<String> beansDefinitionSet = new HashSet<>();
                beansDefinitionSet.add(beanName);
                beansDefinitionMap.put(clazz, beansDefinitionSet);
            }
            List<String> constructorsList = createConstructorsList(confObj, sectionName);
            constructorsDefinitionMap.put(beanName, constructorsList);
        }
        dependencyBuilder = new DependencyBuilder(beansDefinitionMap, constructorsDefinitionMap);
    }

    private List<String> createConstructorsList(HierarchicalINIConfiguration confObj, String sectionName) {

        List<String> constructorsList = new ArrayList<>();

        SubnodeConfiguration sectionBody = confObj.getSection(sectionName);
        Iterator iterator = sectionBody.getKeys();

        while (iterator.hasNext()) {
            Object key = iterator.next();
            String valueName = sectionBody.getString(key.toString());

            constructorsList.add(valueName);
        }
        return constructorsList;
    }
}
