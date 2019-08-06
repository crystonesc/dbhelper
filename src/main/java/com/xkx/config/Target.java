package com.xkx.config;


import com.xkx.Exception.DbhelperException;

public abstract class Target {


    public abstract void writeToTarget() throws DbhelperException;

    public abstract  void createTarget() throws DbhelperException;

    public abstract  void init(Config config) throws DbhelperException;

}
