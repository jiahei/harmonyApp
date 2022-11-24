package com.example.myspring.controller;


import com.example.myspring.domain.Users;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;

@RestController
@RequestMapping("/Usersdata")
@ResponseBody
public class TestDataBase {

    //定义要访问的数据库地址和用户名及密码
    private static String url="此处填写自己的数据库地址";
    private static String user="用户名";
    private static String password="密码";

    @GetMapping
    public Users getById(){
        try {
            //加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection conn=null;
        Statement statement=null;
        ResultSet resultSet=null;
        Users users = null;
        //获取链接
        try {
            conn = DriverManager.getConnection(url,user,password);
            String sql = "SELECT * FROM user";
            statement = conn.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                users = new Users(username,password);
                System.out.println(user);
                System.out.println(password);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //关闭conn
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //关闭statement
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //关闭resultSet
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return users;
    }
}
