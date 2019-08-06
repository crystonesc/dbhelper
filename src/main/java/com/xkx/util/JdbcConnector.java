package com.xkx.util;

import java.sql.*;

public class JdbcConnector {

        private String driverClass;
        private String url;
        private String username;
        private String password;

        public JdbcConnector(String driverClass, String url, String username, String password) {
            this.driverClass = driverClass;
            this.url = url;
            this.username = username;
            this.password = password;
        }


        /*
         * 	注册驱动
         */
        public void loadDriver() throws ClassNotFoundException {
            Class.forName(driverClass);
        }

        /*
         * 获得连接
         */
        public Connection getConnection() throws Exception {
            loadDriver();
            Connection conn = DriverManager.getConnection(url, username, password);
            return conn;
        }

        /*
         * 资源释放
         */
        public void release(Statement stmt, Connection conn) {
            if(stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                stmt = null;
            }
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                conn = null;
            }
        }
        /*
         * 释放资源
         */
        public void release(ResultSet rs, Statement stmt, Connection conn) {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                rs = null;
            }

            if(stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                stmt = null;
            }
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                conn = null;
            }
        }

    /*
     * 资源释放
     */
    public void release(Statement stmt, Connection conn,PreparedStatement cmd) {
        if(stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            stmt = null;
        }
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            conn = null;
        }
        if(cmd != null) {
            try {
                cmd.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            cmd = null;
        }
    }

}
