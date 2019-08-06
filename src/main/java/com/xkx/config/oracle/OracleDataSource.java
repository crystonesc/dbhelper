package com.xkx.config.oracle;

import com.xkx.Exception.DbhelperException;
import com.xkx.config.Config;
import com.xkx.config.DataSource;
import com.xkx.config.ObjectMeta;
import com.xkx.util.CglibBeanUtil;
import com.xkx.util.JdbcConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

public class OracleDataSource extends DataSource {

    private String url;
    private String userName;
    private String password;
    private String tableName;
    private JdbcConnector jdbcConnector;
    private String driverClass = "oracle.jdbc.driver.OracleDriver";



    @Override
    public void init(Config config) throws DbhelperException {
        if(this.url == null || this.url.length()<=0){
            throw new DbhelperException("源数据库URL未设置");
        }
        if(this.userName == null || this.userName.length()<=0){
            throw new DbhelperException("源数据库用户名未设置");
        }
        if(this.password == null || this.password.length()<=0){
            throw new DbhelperException("源数据库密码未设置");
        }
        if(this.tableName == null || this.tableName.length()<=0){
            throw new DbhelperException("源数据库表名未设置");
        }
        jdbcConnector = new JdbcConnector(driverClass,url,userName,password);
        this.getTableSize();
        super.init(config);
    }


    @Override
    public void fillDataList() throws DbhelperException {
        String sql = this.createSelectSql(false);
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = jdbcConnector.getConnection();
            stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                HashMap propertyMap = new HashMap();
                for(ObjectMeta objectMeta :objectMetaArrayList) {
                    String name = objectMeta.getName();
                    String type = objectMeta.getType();
                    propertyMap.put(name, Class.forName(type));
                }
                CglibBeanUtil bean = new CglibBeanUtil(propertyMap);
                for(ObjectMeta objectMeta :objectMetaArrayList) {
                    String name = objectMeta.getName();
                    String type = objectMeta.getType();
                    Object value = resultSet.getObject(name);
                    bean.setValue(name,Class.forName(type).cast(value));
                }
                this.dataList.add(bean.getObject());
            }
        }catch (Exception e) {
            e.printStackTrace();
            throw new DbhelperException("读取源数据失败");

        }finally {
            jdbcConnector.release(stmt,conn);
        }
    }

    @Override
    public void fillDataListInBatch() throws DbhelperException {
        Connection conn = null;
        Statement stmt = null;
        if(rowCount == 0){
            throw new DbhelperException("no data exists in table:"+tableName);
        }
        try {
            this.dataList.clear();
            String sql = this.createSelectSql(true);
            conn = jdbcConnector.getConnection();
            stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);

            while (resultSet.next()) {
                HashMap propertyMap = new HashMap();
                for(ObjectMeta objectMeta :objectMetaArrayList) {
                    String name = objectMeta.getName();
                    String type = objectMeta.getType();
                    propertyMap.put(name, Class.forName(type));
                }
                CglibBeanUtil bean = new CglibBeanUtil(propertyMap);
                for(ObjectMeta objectMeta :objectMetaArrayList) {
                    String name = objectMeta.getName();
                    String type = objectMeta.getType();
                    Object value = resultSet.getObject(name);
                    bean.setValue(name,Class.forName(type).cast(value));
                }
                this.dataList.add(bean.getObject());
            }
            this.position = this.position + size;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String createSelectSql(boolean ifPage){
        String sql;
        if(!ifPage){
            sql = "select * from "+tableName;
        }else{
            sql = "select * from ( select a.*,rownum rn from (select * from "+tableName +") a) where rn>="+position +" and rn<"+(position+size);
        }
        return sql;
    }

    private void getTableSize() throws DbhelperException {
        String sql = "select count(0) from "+tableName;
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = jdbcConnector.getConnection();
            stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            if(resultSet.next())
            {
                this.rowCount=resultSet.getInt(1);
             }
        }catch (Exception e) {
            e.printStackTrace();
            throw new DbhelperException("获取表大小失败");
        }finally {
            jdbcConnector.release(stmt,conn);
        }

    }


    @Override
    public String toString() {
        return "OracleDataSource{" +
                "url='" + url + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", tableName='" + tableName + '\'' +
                ", jdbcConnector=" + jdbcConnector +
                ", driverClass='" + driverClass + '\'' +
                ", parent='" + super.toString() + '\'' +
                '}';
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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

}
