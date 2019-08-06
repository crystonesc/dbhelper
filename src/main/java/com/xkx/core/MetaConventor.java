package com.xkx.core;

import com.xkx.Exception.DbhelperException;
import com.xkx.config.Meta;
import com.xkx.config.ObjectMeta;
import com.xkx.config.mysql.MysqlMeta;

import java.util.ArrayList;
import java.util.List;

public class MetaConventor {

    public static List<ObjectMeta> MysqlToObject(List<Meta> metaList) throws DbhelperException {
        List<ObjectMeta> objectMetaArrayList = new ArrayList<>();
        for(Meta meta:metaList){
            ObjectMeta objectMeta = new ObjectMeta();
            objectMeta.setName(((MysqlMeta) meta).getColName());
            objectMeta.setNumber(((MysqlMeta) meta).getColNumber());
            String mysqlType = ((MysqlMeta) meta).getType().toUpperCase();
            switch(mysqlType){
                case "VARCHAR":
                case "CHAR":
                case "TEXT" :
                case "TINYTEXT":
                    objectMeta.setType("java.lang.String");
                    break;
                case "TINYINT":
                case "SMALLINT":
                case "MEDIUMINT":
                case "INT":
                    objectMeta.setType("java.lang.Integer");
                    break;
                case "BIGINT":
                    objectMeta.setType("java.lang.Long");
                    break;
                case "FLOAT":
                    objectMeta.setType("java.lang.Float");
                    break;
                case "DOUBLE":
                    objectMeta.setType("java.lang.Double");
                    break;
                default:
                    throw new DbhelperException(mysqlType+"不是Mysql数据类型");
            }
            objectMetaArrayList.add(objectMeta);
        }
        return objectMetaArrayList;
    }

}
