package com.yejh.dbinit;//package com.yejh.dbinit;/**
// * @author yejh
// * @create 2020-02_16 13:59
// */
//
//import org.apache.commons.dbutils.QueryRunner;
//import org.junit.jupiter.api.Test;
//
//import java.io.*;
//import java.sql.*;
//import java.util.*;
//
//import static java.util.jar.Pack200.Packer.PASS;
//
///**
// * @description: TODO
// **/
//public class DatabaseInitializer {
//    private final static String URL = "jdbc:mysql://localhost:3306/library?serverTimezone=Asia/Shanghai";
//    private final static String DRIVERCLASS = "com.mysql.cj.jdbc.Driver";
//    private final static String USERNAME = "root";
//    private final static String PASSWORD = "861861";
//
//    protected static QueryRunner queryRunner = new QueryRunner();
//
//
//    public static void insertAuthor(Map<String, List<Long>> authors) throws ClassNotFoundException, SQLException {
//        Connection connection = null;
//        PreparedStatement pstmt = null;
//        try {
//            // a.导入驱动，加载具体的驱动类
//            Class.forName(DRIVERCLASS);// 加载具体的驱动类
//            // b.与数据库建立连接
//            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
//
//			/* Statement
//			// c.发送sql，执行(增删改、查)
//			stmt = connection.createStatement();
//			//String sql = "insert into student values(1,'zs',23,'s1')";
////			String sql = "update student set STUNAME='ls' where stuno=1";
//			String sql = "delete from student where stuno=1";
//			// 执行SQL
//			int count = stmt.executeUpdate(sql); // 返回值表示 增删改 几条数据
//			*/
//            //PreparedStatement
//
//            String sql = "insert into t_author(aut_name, aut_record) values(?,?)";
//            int len = 0;
//            for (Map.Entry<String, List<Long>> entry : authors.entrySet()) {
//                len += entry.getValue().size();
//            }
//
//            Object[][] params = new Object[len][2];
//            int i = 0;
//            for (Map.Entry<String, List<Long>> entry : authors.entrySet()) {
//                String name = entry.getKey();
//                List<Long> value = entry.getValue();
//                if (name.length() > 100) {
//                    System.out.println("作者名超出长度: " + name);
//                }
//                for (Long l : value) {
//
//                    Object[] oneParams = params[i];
//                    oneParams[0] = name;
//                    oneParams[1] = l;
//                    i++;
//                }
//            }
//            queryRunner.batch(connection, sql, params);
//
//
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (pstmt != null) pstmt.close();// 对象.方法
//                if (connection != null) connection.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public static void insertTitle(Map<String, Long> titles) throws ClassNotFoundException, SQLException {
//        Connection connection = null;
//        PreparedStatement pstmt = null;
//        try {
//            // a.导入驱动，加载具体的驱动类
//            Class.forName(DRIVERCLASS);// 加载具体的驱动类
//            // b.与数据库建立连接
//            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
//
//            String sql = "insert into t_title(tit_name, tit_record) values(?,?)";
//            Object[][] params = new Object[titles.size()][2];
//            int i = 0;
//            for (Map.Entry<String, Long> entry : titles.entrySet()) {
//                String name = entry.getKey();
//                Long value = entry.getValue();
//                if (name.length() > 200) {
//                    System.out.println("标题超出长度: " + name);
//                }
//
//                Object[] oneParams = params[i];
//                oneParams[0] = name;
//                oneParams[1] = value;
//                i++;
//
//            }
//            queryRunner.batch(connection, sql, params);
//
//
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (pstmt != null) pstmt.close();// 对象.方法
//                if (connection != null) connection.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
////    @Test
////    public void test() throws Exception {
////        ArrayList<Long> longs = new ArrayList<>();
////        Collections.addAll(longs, 1L, 2L, 3L);
////        authorIndex.put("abc", longs );
////        insertAuthor(authorIndex);
////    }
//
//
//}
