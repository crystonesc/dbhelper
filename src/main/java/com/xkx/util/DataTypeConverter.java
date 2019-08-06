package com.xkx.util;

public class DataTypeConverter {

    public static String Java2Mysql(String javaType,int length){
        switch(javaType){
            case "java.lang.String":
                if(length  == -1){
                    return "text";
                }else{
                    return "varchar";
                }
            case "java.lang.Integer":
                return "int";
            case "java.lang.Long":
                return "bigint";
            case "java.lang.Double":
                return "double";
            case "java.lang.Float":
                return "float";
            default:
                return null;
        }
    }

    public static String Mysql2Java(String mysqlType){
        switch(mysqlType.toLowerCase()){
            case "varchar":
            case "text":
                return "java.lang.String";
            case "int":
                return "java.lang.Integer";
            case "bigint":
                return "java.lang.Long";
            case "double":
                return "java.lang.Double";
            case "float":
                return "java.lang.Float";
            default:
                return null;
        }
    }
}
