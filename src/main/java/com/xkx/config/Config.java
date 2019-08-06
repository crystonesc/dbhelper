package com.xkx.config;


public class Config {

    private Type type;
    private String name;
    private DataSource dataSource;
    private Mapping mapping;
    private Target target;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Mapping getMapping() {
        return mapping;
    }

    public void setMapping(Mapping mapping) {
        this.mapping = mapping;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "Config{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", dataSource=" + dataSource +
                ", mapping=" + mapping +
                ", target=" + target +
                '}';
    }





}
