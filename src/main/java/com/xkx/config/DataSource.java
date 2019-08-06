package com.xkx.config;

import com.xkx.Exception.DbhelperException;
import com.xkx.core.MetaConventor;

import java.util.ArrayList;
import java.util.List;

public abstract class DataSource {




    protected ArrayList<Object> dataList = new ArrayList<>();
    protected List<ObjectMeta> objectMetaArrayList;
    protected Config config;
    protected int position = 0;
    protected int size = 5000;
    protected int rowCount = 0;


    public void init(Config config) throws DbhelperException {
        this.config = config;
        Type type = config.getType();
        List<Meta> metaList = config.getMapping().getMetaList();
        switch (type) {
            case TXT2MYSQL:
            case ORACLE2MYSQL:
                objectMetaArrayList = MetaConventor.MysqlToObject(metaList);
                break;
            default:
                objectMetaArrayList = null;
                break;
        }
    }

    public abstract void fillDataList() throws DbhelperException;

    public abstract void fillDataListInBatch() throws DbhelperException;

    public ArrayList<Object> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<Object> dataList) {
        this.dataList = dataList;
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "DataSource{" +
                "dataList=" + dataList +
                ", objectMetaArrayList=" + objectMetaArrayList +
                ", config=" + config +
                ", position=" + position +
                ", size=" + size +
                '}';
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }
}
