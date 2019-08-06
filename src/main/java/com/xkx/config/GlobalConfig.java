package com.xkx.config;

import com.xkx.config.mysql.MysqlMeta;
import com.xkx.config.mysql.MysqlTarget;
import com.xkx.config.oracle.OracleDataSource;
import com.xkx.config.oracle.OracleMeta;
import com.xkx.config.text.TextMeta;
import com.xkx.config.text.TextDataSource;

import java.util.HashMap;
import java.util.Map;

public class GlobalConfig {

    public static Map<Type,Class> dataSourceMap = new HashMap<Type,Class>();
    public static Map<Type,Class> targetMap = new HashMap<Type,Class>();
    public static Map<Type,Class> metaMap = new HashMap<>();

    static {
        dataSourceMap.put(Type.TXT2MYSQL,TextDataSource.class);
        dataSourceMap.put(Type.ORACLE2MYSQL,OracleDataSource.class);
        metaMap.put(Type.TXT2MYSQL,MysqlMeta.class);
        metaMap.put(Type.ORACLE2MYSQL,MysqlMeta.class);
        targetMap.put(Type.TXT2MYSQL,MysqlTarget.class);
        targetMap.put(Type.ORACLE2MYSQL,MysqlTarget.class);
    }
}
