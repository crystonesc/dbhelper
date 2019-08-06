package com.xkx.config.mysql;

import com.xkx.Exception.DbhelperException;
import com.xkx.config.*;
import com.xkx.util.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;


public class MysqlTarget extends Target {

    private String url;
    private String userName;
    private String password;
    private String driverClass = "com.mysql.cj.jdbc.Driver";
    private String tableName;
    private JdbcConnector jdbcConnector;
    private Config config;
    private int batchSize = 1000;


    @Override
    public void init(Config config) throws DbhelperException {
        if(url == null || url.length()<=0){
            throw new DbhelperException("未指定目标Mysql的URL地址");
        }
        if(userName == null || userName.length()<=0){
            throw new DbhelperException("未指定目标Mysql的账号");
        }
        if(password == null || password.length()<=0){
            throw new DbhelperException("未指定目标Mysql的密码");
        }
        this.jdbcConnector = new JdbcConnector(driverClass,url,userName,password);
        this.config = config;
    }


    @Override
    public void writeToTarget() throws DbhelperException {
        Connection conn = null;
        PreparedStatement cmd = null;
        List<Object> dataList = config.getDataSource().getDataList();
        List<Meta> metaList = config.getMapping().getMetaList();
        String insertSql = createInsertSql(config.getMapping().getName(),metaList);
        try {
            conn = jdbcConnector.getConnection();
            conn.setAutoCommit(false);
            cmd = conn.prepareStatement(insertSql);
            int counter = 0;
            for(Object obj:dataList){
                for(int index = 0;index<metaList.size();index++){
                    String colName = metaList.get(index).getColName();
                    String methodName = "get"+StringUtil.chnageFirstLetterToUpperCase(colName);
                    Method method = obj.getClass().getMethod(methodName);
                    Field[] fieldList = obj.getClass().getDeclaredFields();
                    String type = obj.getClass().getDeclaredField("$cglib_prop_"+colName).getName().getClass().getSimpleName();
                    Object value = method.invoke(obj,null);
                    JdbcUtil.setValueByType(type,cmd,index+1,value);
                }
                counter++;
                cmd.addBatch();
                if(counter%this.batchSize == 0){
                    cmd.executeBatch();
                    conn.commit();
                    cmd.clearBatch();
                }
                cmd.executeBatch();
                conn.commit();
                cmd.clearBatch();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DbhelperException("目标导入数据失败");
        }finally {
            jdbcConnector.release(cmd,conn,cmd);
        }
    }

    @Override
    public void createTarget() throws DbhelperException {
        Connection conn = null;
        Statement stmt = null;
        this.tableName = config.getMapping().getName();
        List<Meta> metaList = config.getMapping().getMetaList();
        String creatTableSql = this.createNewTableSql(tableName,metaList);
        String dropTableSql = this.createDropSql(tableName);
        try {
            conn = jdbcConnector.getConnection();
            stmt = conn.createStatement();
            stmt = conn.createStatement();
            stmt.execute(dropTableSql);
            stmt.execute(creatTableSql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DbhelperException("目标建表失败");
        }finally {
            jdbcConnector.release(stmt,conn);
        }


    }





    private String createDropSql(String tableName){
        return "DROP TABLE IF EXISTS "+"`"+tableName+"`";
    }

    private String createNewTableSql(String tableName,List<Meta> metaList){
        StringBuffer sBuffer = new StringBuffer("CREATE TABLE `");
        sBuffer.append(tableName);
        sBuffer.append("`  (");
        sBuffer.append("`id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,");
        for(Meta meta:metaList){
            String isNull = "NULL";
            String lengthAndAccuracy = "";
            String colName = ((MysqlMeta) meta).getColName();
            String type = ((MysqlMeta) meta).getType();
            int length = ((MysqlMeta) meta).getLength();
            int accuracy = ((MysqlMeta) meta).getAccuracy();
            if(accuracy == 0){
                lengthAndAccuracy = "("+length+") ";
            }else{
                lengthAndAccuracy = "("+length+", "+accuracy+") ";
            }

            if(((MysqlMeta) meta).isNotNull()){
                isNull = "NOT NULL";
            }
            String comment = ((MysqlMeta) meta).getComment();
            sBuffer.append("`"+colName+"` "+type+lengthAndAccuracy+isNull+" COMMENT '"+comment+"',");
        }
        sBuffer.append("PRIMARY KEY (`id`) USING BTREE");
        sBuffer.append(") ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;");
        return sBuffer.toString();
    }

    private String createInsertSql(String tableName,List<Meta> metaList){
        StringBuffer sBuffer2 = new StringBuffer("(");
        StringBuffer sBuffer1 = new StringBuffer("INSERT INTO `");
        sBuffer1.append(tableName);
        sBuffer1.append("` (");
        for(int i=0;i<metaList.size();i++){
            String colName = ((MysqlMeta) metaList.get(i)).getColName();
            if(i == (metaList.size()-1)){
                sBuffer1.append(colName+") values ");
                sBuffer2.append("?);");
            }else {
                sBuffer1.append(colName+",");
                sBuffer2.append("?,");
            }
        }
        return sBuffer1.toString()+sBuffer2.toString();
    }



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public JdbcConnector getJdbcConnector() {
        return jdbcConnector;
    }

    public void setJdbcConnector(JdbcConnector jdbcConnector) {
        this.jdbcConnector = jdbcConnector;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    @Override
    public String toString() {
        return "MysqlTarget{" +
                "url='" + url + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", driverClass='" + driverClass + '\'' +
                ", tableName='" + tableName + '\'' +
                ", jdbcConnector=" + jdbcConnector +
                ", config=" + config +
                '}';
    }

}
