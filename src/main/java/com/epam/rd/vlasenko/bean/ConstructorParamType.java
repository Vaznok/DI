package com.epam.rd.vlasenko.bean;

public enum ConstructorParamType {
    VAL("val"), REF("ref");

    private final String typeName;

    ConstructorParamType(String typeName) {
        this.typeName = typeName;
    }

    public static ConstructorParamType fromString(String typeName) {
        for (ConstructorParamType type : ConstructorParamType.values()) {
            if (type.typeName.equalsIgnoreCase(typeName)) {
                return type;
            }
        }
        throw new IllegalArgumentException(String.format("No enum constant %s in %s", typeName, ConstructorParamType.class));
    }

    public String getText() {
        return this.typeName;
    }

    @Override
    public String toString() {
        return typeName;
    }
}
