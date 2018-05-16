package com.epam.rd.vlasenko.bean;

public class ConstructorParam {
    private String id;
    private String value;
    private ConstructorParamType type;

    public ConstructorParam(String id, String value, ConstructorParamType type) {
        this.id = id;
        this.value = value;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public ConstructorParamType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConstructorParam that = (ConstructorParam) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
