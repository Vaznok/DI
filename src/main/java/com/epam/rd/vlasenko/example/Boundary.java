package com.epam.rd.vlasenko.example;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class Boundary {

    public void hello(String name, int age) {

    }

    public static void main(String[] args) {
        Method[] methods = Boundary.class.getMethods();
        for (Method method : methods) {
            System.out.print(method.getName() + "(");
            Parameter[] parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                System.out.print(parameter.getType().getName() + " " + parameter.getName() + " ");
            }
            System.out.println(")");
        }
    }
}
