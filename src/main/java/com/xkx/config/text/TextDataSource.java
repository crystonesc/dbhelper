package com.xkx.config.text;


import com.xkx.Exception.DbhelperException;
import com.xkx.config.*;
import com.xkx.core.MetaConventor;
import com.xkx.util.CglibBeanUtil;
import com.xkx.util.FileUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TextDataSource extends DataSource {


    private String dataFilePath;
    private String seperator;


    public String getDataFilePath() {
        return dataFilePath;
    }

    public void setDataFilePath(String dataFilePath) {
        this.dataFilePath = dataFilePath;
    }

    public String getSeperator() {
        return seperator;
    }

    public void setSeperator(String seperator) {
        this.seperator = seperator;
    }

    @Override
    public void init(Config config) throws DbhelperException {
        if(dataFilePath == null || dataFilePath.length()<=0){
            throw new DbhelperException("源文件地址未设置");
        }
        if(seperator == null || seperator.length()<=0){
            throw new DbhelperException("源文件中数据分割符未设置");
        }
        super.init(config);
    }


    @Override
    public void fillDataList() throws DbhelperException{
        BufferedReader reader = null;
        try{
            reader = FileUtil.buildBufferedReader(dataFilePath);
            String row;
            while((row = reader.readLine()) != null){
                this.handleDataRow(row);
            }
        }catch (DbhelperException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void fillDataListInBatch() throws DbhelperException {
        if(size == 0){
            throw new DbhelperException("请指定每次读取行数");
        }
        BufferedReader reader = null;
        try{
            reader = FileUtil.buildBufferedReader(dataFilePath);
            int lineNumber = 0;
            String row;
            while((row = reader.readLine()) != null){
                if(lineNumber < this.position){
                    continue;
                }
                lineNumber++;
                if(lineNumber >=  position && lineNumber < position+size){
                    this.handleDataRow(row);
                }
                position = position+size;
                reader.close();
                break;
            }
        }catch(DbhelperException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void handleDataRow(String row) throws DbhelperException {
        String[] rowArray = row.split(seperator);
        HashMap propertyMap = new HashMap();
        dataList = new ArrayList<>();
        for(ObjectMeta objectMeta :objectMetaArrayList) {
            String name = objectMeta.getName();
            String type = objectMeta.getType();
            try {
                propertyMap.put(name, Class.forName(type));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new DbhelperException("无法找到类:"+ type);
            }
        }
        CglibBeanUtil bean = new CglibBeanUtil(propertyMap);
        for(ObjectMeta objectMeta :objectMetaArrayList) {
            String name = objectMeta.getName();
            String type = objectMeta.getType();
            int number = objectMeta.getNumber();
            if (!type.equals("java.lang.String")) {
                try {
                    bean.setValue(name, Class.forName(type).getMethod("valueOf", String.class).invoke(null, rowArray[number]));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new DbhelperException("出错啦:"+ e.getMessage());
                } catch (InvocationTargetException e) {
                    throw new DbhelperException("出错啦:"+ e.getMessage());
                } catch (NoSuchMethodException e) {
                    throw new DbhelperException("找不到该方法:"+ e.getMessage());
                } catch (ClassNotFoundException e) {
                    throw new DbhelperException("无法找到类:"+ type);
                }
            } else {
                bean.setValue(name, rowArray[number]);
            }
        }
        dataList.add(bean.getObject());
    }




}
