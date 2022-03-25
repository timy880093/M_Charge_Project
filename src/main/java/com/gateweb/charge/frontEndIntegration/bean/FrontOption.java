package com.gateweb.charge.frontEndIntegration.bean;

public class FrontOption {
    String id;
    String name;

    public FrontOption() {
    }

    public FrontOption(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Option{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
