package com.xkx.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xkx.Exception.DbhelperException;
import com.xkx.config.*;
import com.xkx.util.FileUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class TransExecutor {


    private static final Logger logger = Logger.getLogger(TransExecutor.class);
    public TransExecutor(String configFilePath) {
        this.configFilePath = configFilePath;
    }

    private String configFilePath;
    private Config config = new Config();
    private Type type;



    public void init() throws DbhelperException {
        String jsonContent = FileUtil.ReadFile(this.configFilePath);
        JSONObject jsonObject = JSON.parseObject(jsonContent);
        String typeStr = jsonObject.getString("type");
        try{
            type = Type.valueOf(typeStr);
        }catch(RuntimeException e) {
            throw new DbhelperException("任务类型必须是: TXT2MYSQL,TXT2ORACLE,MYSQL2TXT,MYSQL2ORACLE,ORACLE2TXT,ORACLE2MYSQL的一种");
        }
        Class<DataSource>  dataSourceCls = GlobalConfig.dataSourceMap.get(type);
        Class<Target>  targetCls = GlobalConfig.targetMap.get(type);
        if(jsonObject.getString("name") == null || jsonObject.getString("name").length()<=0){
            throw new DbhelperException("请指定任务名称");
        }
        config.setName(jsonObject.getString("name"));
        config.setType(type);
        config.setDataSource(JSON.parseObject(jsonObject.getString("datasource"),dataSourceCls));
        Mapping mapping = new Mapping();
        if(jsonObject.getString("mapping") == null || jsonObject.getString("mapping").length()<=0){
            throw new DbhelperException("请指定源数据到目标数据的映射关系");
        }
        if(JSON.parseObject(jsonObject.getString("mapping")).getString("name") == null || JSON.parseObject(jsonObject.getString("mapping")).getString("name").length()<=0){
            throw new DbhelperException("请指定映射名称");
        }
        mapping.setName(JSON.parseObject(jsonObject.getString("mapping")).getString("name"));
        if(JSON.parseObject(jsonObject.getString("mapping")).getString("metaList") == null || JSON.parseObject(jsonObject.getString("mapping")).getString("metaList").length()<=0){
            throw new DbhelperException("请指定源数据到目标的映射关系");
        }
        JSONArray metaListArray = JSON.parseArray(JSON.parseObject(jsonObject.getString("mapping")).getString("metaList"));
        List<Meta> metaList = new ArrayList<Meta>();
        Class<Meta> metaCls = GlobalConfig.metaMap.get(type);
        for(int i=0;i<metaListArray.size();i++){
            metaList.add(JSON.parseObject(metaListArray.get(i).toString(),metaCls));
        }
        mapping.setMetaList(metaList);
        config.setMapping(mapping);
        if(jsonObject.getString("target") == null || jsonObject.getString("target").length()<=0){
            throw new DbhelperException("请指定目标的信息");
        }
        config.setTarget(JSON.parseObject(jsonObject.getString("target"),targetCls));

    }


    private void beginTransData(DataSource dataSource,Target target) throws DbhelperException {
        logger.info("step1:init dataSource");
        dataSource.init(this.config);
        logger.info("step2:load data into dataSource");
        dataSource.fillDataList();
        logger.info("step3:init target");
        target.init(this.config);
        logger.info("step4:create target");
        target.createTarget();
        logger.info("step5:write to target");
        target.writeToTarget();
    }

    private void beginTransDataInBatch(DataSource dataSource,Target target) throws DbhelperException {
        logger.info("step1:init dataSource");
        dataSource.init(this.config);
        logger.info("step2:init target");
        target.init(this.config);
        logger.info("step3:create target");
        target.createTarget();
        int rowCount  = dataSource.getRowCount();
        int position = dataSource.getPosition();
        int size = dataSource.getSize();
        logger.info("Begin to transfer data,total count is:"+rowCount);
        while(position<=rowCount){
            logger.info("Begin to load the data from "+position+" to "+(position+size));
            dataSource.fillDataListInBatch();
            logger.info("Begin to write the data from "+position+" to "+(position+size));
            target.writeToTarget();
            position = dataSource.getPosition();
        }
    }

    public static void main(String[] args) throws DbhelperException {
        String configPath = args[0];
        TransExecutor transExecutor  = new TransExecutor(configPath);
        transExecutor.init();
        //transExecutor.beginTransData(transExecutor.config.getDataSource(),transExecutor.config.getTarget());
        transExecutor.beginTransDataInBatch(transExecutor.config.getDataSource(),transExecutor.config.getTarget());
    }


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

}
