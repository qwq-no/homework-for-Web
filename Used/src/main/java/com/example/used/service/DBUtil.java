package com.example.used.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
    private static final String URL = "jdbc:mysql://10.100.164.21:3306/secondhand?useSSL=false&serverTimezone=Asia/Shanghai";
    private static final String USER = "wang";
    private static final String PASSWORD = "123456";

    // 静态代码块：加载驱动（MySQL 8+ 可省略，但保留更兼容）
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * 安全关闭资源（按顺序：ResultSet → Statement → Connection）
     */
    public static void close(ResultSet rs, Statement stmt, Connection conn) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        if (stmt != null) {
            try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        if (conn != null) {
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // 重载：无 ResultSet 的情况
    public static void close(Statement stmt, Connection conn) {
        close(null, stmt, conn);
    }
}