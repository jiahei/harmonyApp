package com.example.myspring.service;


import com.example.myspring.domain.PostInfo;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;

@Service
@Component
public class UserService {

    //定义要访问的数据库地址和用户名及密码
    private static String url="此处填写自己的数据库地址";
    private static String user="用户名";
    private static String password="密码";

    // 查询组队信息的表，并将查到的数据返回
    public ArrayList<PostInfo> getInfo() {
        try {
            //加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection conn=null;
        Statement statement=null;
        ResultSet resultSet=null;
        ArrayList<PostInfo> info = new ArrayList<PostInfo>();;
        //获取链接
        try {
            conn = DriverManager.getConnection(url,user,password);
            //选中组队信息的表
            String sql = "SELECT * FROM postinfo";
            statement = conn.createStatement();
            //执行sql语句
            resultSet = statement.executeQuery(sql);
            //将查到的数据保存在info中
            while (resultSet.next()){
                Integer uid = resultSet.getInt("uid");
                String sportType = resultSet.getString("sportType");
                String time = resultSet.getString("time");
                String level = resultSet.getString("level");
                String location = resultSet.getString("location");
                int numNeeded = resultSet.getInt("numNeeded");
                PostInfo temp = new PostInfo(uid,sportType,time,location,level,numNeeded);
                info.add(temp);
                System.out.println(info);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //关闭conn
            try {
                if (conn!=null){
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //关闭statement
            try {
                if (statement!=null){
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //关闭resultSet
            try {
                if (resultSet!=null){
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return info;
    }

    //向组队信息的表中增加数据
    public String addInfo(String sportType,String time,String level,String location,int numNeeded){
        String status = "notOk";
        Connection conn=null;
        Statement statement=null;
        int resultSet=-1;
        try {
            conn = DriverManager.getConnection(url,user,password);
            String sql = "INSERT INTO postinfo (sportType,time,`level`,location,numNeeded)" +
                    " VALUES ("+"\""+sportType+"\""+","+"\""+time+"\""+","+"\""+level+"\""+","+"\""+location+"\""+","+"\""+numNeeded+"\""+")";
            statement = conn.createStatement();
            resultSet = statement.executeUpdate(sql);
            if (resultSet>0){
                status = "OK";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //关闭conn
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //关闭statement
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return status;
    }

    //通过用户名在用户信息的表中查询，若存在就返回密码
    public String queryUserByUsername(String username){
        try {
            //加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String pwd = "notExist";
        Connection conn=null;
        Statement statement=null;
        ResultSet resultSet=null;
        //获取链接
        try {
            conn = DriverManager.getConnection(url,user,password);
            String sql = "SELECT * FROM user Where username = " + username;
            statement = conn.createStatement();
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()){
                System.out.println(resultSet+"--------------------");
                pwd = resultSet.getString("password");
                System.out.println(pwd+"--------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //关闭conn
            try {
                if (conn!=null){
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //关闭statement
            try {
                if (statement!=null){
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //关闭resultSet
            try {
                if (resultSet!=null){
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return pwd;
    }

    //根据传入的组队信息的id查询组队信息的表，返回需求人数的值
    public int queryInfoByUid(int uid){
        try {
            //加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        int pwd = -1;
        Connection conn=null;
        Statement statement=null;
        ResultSet resultSet=null;
        //获取链接
        try {
            conn = DriverManager.getConnection(url,user,password);
            String sql = "SELECT * FROM postinfo Where uid = " + uid;
            statement = conn.createStatement();
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()){
                System.out.println(resultSet+"--------------------");
                pwd = resultSet.getInt("numNeeded");
                System.out.println(pwd+"--------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //关闭conn
            try {
                if (conn!=null){
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //关闭statement
            try {
                if (statement!=null){
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //关闭resultSet
            try {
                if (resultSet!=null){
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return pwd;
    }
}
