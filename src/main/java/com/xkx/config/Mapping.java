package com.xkx.config;

import java.util.List;

public class Mapping {

    private String name;
    private List<Meta> metaList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Meta> getMetaList() {
        return metaList;
    }

    public void setMetaList(List<Meta> metaList) {
        this.metaList = metaList;
    }

    @Override
    public String toString() {
        return "Mapping{" +
                "name='" + name + '\'' +
                ", metaList=" + metaList +
                '}';
    }
}
