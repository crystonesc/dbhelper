package com.xkx.config;

public class Meta {

    protected int colNumber;
    protected String colName;
    protected String type;
    protected int length = 10;
    protected int accuracy = 0;

    public int getColNumber() {
        return colNumber;
    }

    public void setColNumber(int colNumber) {
        this.colNumber = colNumber;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    @Override
    public String toString() {
        return "Meta{" +
                "colNumber=" + colNumber +
                ", colName='" + colName + '\'' +
                ", type='" + type + '\'' +
                ", length=" + length +
                ", accuracy=" + accuracy +
                '}';
    }





}
