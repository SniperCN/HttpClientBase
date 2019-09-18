package com.xuehai.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xuehai.base.Log;
import java.sql.*;

/**
 * @ClassName MySQLUtil
 * @Description:    关系型数据库工具类
 * @Author Sniper
 * @Date 2019/4/16 17:16
 */
public class MySQLUtil {

    private static final String CLASS_NAME = MySQLUtil.class.getName();
    private static Connection conn;
    private static PreparedStatement pstat;
    private static ResultSet rs;

    /**
     *
     * @Description:                        数据库查询
     * @param dbSource						数据源
     * @param sql							sql语句
     * @return JSONArray	                返回json数组
     */
    public static JSONArray query(String dbSource, String sql) {
        JSONArray array = new JSONArray();
        try {
            try {
                Log.info(CLASS_NAME, "执行 sql: {}", sql);
                conn = getConnection(dbSource);
                pstat = conn.prepareStatement(sql);
                rs = pstat.executeQuery();
                ResultSetMetaData rsMetaData = rs.getMetaData();
                int mdColumns = rsMetaData.getColumnCount();
                while (rs.next()) {
                    JSONObject row = new JSONObject();
                    for (int i = 1; i <= mdColumns; i++) {
                        row.put(rsMetaData.getColumnName(i), rs.getObject(i));
                    }
                    array.add(row);
                }
            } finally {
                closeConnection();
            }
        }catch (SQLException e) {
            Log.error(CLASS_NAME, "数据库异常", e);
        }
        return array;
    }

    /**
     *
     * @Description:        数据库执行
     * @param dbSource		数据源
     * @param sql			sql语句
     * @return void
     */
    public static void execute(String dbSource, String sql) {
        try {
            try {
                Log.info(CLASS_NAME, "执行sql: {}", sql);
                conn = getConnection(dbSource);
                pstat = conn.prepareStatement(sql);
                pstat.execute();
            } finally {
                closeConnection();
            }
        } catch (SQLException e) {
            Log.error(CLASS_NAME, "数据库异常", e);
        }
    }

    /**
     *
     * @Description:        连接数据库并返回
     * @param dbSource		数据源
     * @return Connection	返回数据库连接
     */
    public static Connection getConnection(String dbSource) {
        Log.info(CLASS_NAME, "开始连接数据库,当前dbSource: {}", dbSource);
        try {
            if (conn==null || conn.isClosed()) {
                JSONObject source = JSONObject.parseObject(dbSource);
                String driverName = source.getString("driver");
                String url = source.getString("url");
                String user = source.getString("user");
                String password = source.getString("password");
                Class.forName(driverName);
                conn = DriverManager.getConnection(url, user, password);
            }
        } catch (ClassNotFoundException e) {
            Log.error(CLASS_NAME, "数据库连接异常", e);
        } catch (SQLException e) {
            Log.error(CLASS_NAME, "数据库连接异", e);
        }
        return conn;
    }

    /**
     *
     * @Description:    关闭数据库连接
     * @return void
     */
    @SuppressWarnings("Duplicates")
    public static void closeConnection() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (pstat != null) {
                pstat.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            Log.error(CLASS_NAME, "数据库关闭异常", e);
        }
    }
}
