package com.xkx.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class JdbcUtil {

    public static void setValueByType(String type,PreparedStatement cmd,int index,Object value) throws SQLException {
        switch(type){
            case "String":
                if(value == null){
                    cmd.setNull(index, Types.VARCHAR);
                    return;
                }
                cmd.setString(index,value.toString());
                break;
            case "Integer":
                if(value == null){
                    cmd.setNull(index, Types.INTEGER);
                    return;
                }
                cmd.setInt(index,Integer.valueOf(value.toString()));
                break;
            case "Float":
                if(value == null){
                    cmd.setNull(index, Types.FLOAT);
                    return;
                }
                cmd.setFloat(index,Float.valueOf(value.toString()));
                break;
            case "Double":
                if(value == null){
                    cmd.setNull(index, Types.DOUBLE);
                    return;
                }
                cmd.setDouble(index,Double.valueOf(value.toString()));
                break;
            case "Long":
                if(value == null){
                    cmd.setNull(index, Types.BIGINT);
                    return;
                }
                cmd.setLong(index,Long.valueOf(value.toString()));
                break;
            default:
                break;
        }

    }
}
