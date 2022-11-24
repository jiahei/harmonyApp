package com.example.myspring.controller;

import com.example.myspring.domain.PostInfo;
import com.example.myspring.domain.response;
import com.example.myspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private response res;
    //定义要访问的数据库地址和用户名及密码
    private static String url="此处填写自己的数据库地址";
    private static String user="用户名";
    private static String password="密码";

    //返回组队信息
    @GetMapping("/info")
    public @ResponseBody response getInfoList(){
        try {
            //调用getInfo方法，查询组队信息并返回
            ArrayList<PostInfo> result = userService.getInfo();
            res.setResult(result);
            res.setCode(10000);
            res.setMsg("查询成功");

        }
        catch(DataAccessException e) {
            e.printStackTrace();

            String exceptionMsg = e.getRootCause().getMessage();
            System.out.println("ERROR:" + exceptionMsg);
            res.setCode(500);
            res.setMsg(exceptionMsg);
        }

        return res;
    }

    //向数据库中的组队信息的表添加信息
    @PostMapping("/addinfo")
    public String addInfo(@RequestParam(name = "sportType") String sportType,
                          @RequestParam(name = "time") String time,
                          @RequestParam(name = "location") String location,
                          @RequestParam(name = "level") String level,
                          @RequestParam(name = "numNeeded") int numNeeded){
        //从url中获取信息，调用addInfo方法将数据存入数据库中
        String result = userService.addInfo(sportType,time,level,location,numNeeded);
        return result;
    }

    //登录验证
    @RequestMapping(value="/login",method= RequestMethod.GET)
    public String  login(@RequestParam String username){
        String result = null;
        try {
            //通过url中的用户名信息，调用queryUserByUsername查询密码并返回
            result = userService.queryUserByUsername(username);
            res.setResult(result);
            res.setCode(10000);
            res.setMsg("查询成功");

        }
        catch(DataAccessException e) {
            e.printStackTrace();
            String exceptionMsg = e.getRootCause().getMessage();
            System.out.println("ERROR:" + exceptionMsg);
            res.setCode(500);
            res.setMsg(exceptionMsg);
        }
        return result;
    }

    //注册验证
    @GetMapping("/register")
    public String register(@RequestParam(name = "username") String username,
                           @RequestParam(name = "password") String pwd) {
        //定义返回数据
        String status = "notOk";
        //首先根据用户名进行查询看是否已存在用户名
        String result = userService.queryUserByUsername(username);
        //如果该用户名不存在，就将url中的用户名和密码信息存入数据库
        if(result.equals("notExist")){
            Connection conn=null;
            Statement statement=null;
            int resultSet=-1;
            try {
                conn = DriverManager.getConnection(url,user,password);
                String sql = "INSERT INTO user (username,`password`) VALUES ("+username+","+pwd+")";
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
        }
        return status;
    }

    //更新需求人数
    @PostMapping("/updateNum")
    public String  updateInfo1(@RequestParam(name = "uid") int uid,
                               @RequestParam(name = "method") String method) {
        //定义返回数据
        String res = "notOK";
        //根据url中uid定位到数据的位置
        int result = userService.queryInfoByUid(uid);
        if(result>=0){
            Connection conn=null;
            Statement statement=null;
            //根据url中的增加或者减少，对数据库中需求人数的数量进行增加或者减少操作
            if (method.equals("add")){
                try {
                    int num = result+1;
                    System.out.println(num+"+++++++++++++++++++++++++++++");
                    conn = DriverManager.getConnection(url,user,password);
                    String sql = "UPDATE postinfo SET numNeeded="+num +" WHERE uid="+uid+";";
                    System.out.println(sql);
                    statement = conn.createStatement();
                    if (statement.executeUpdate(sql)>0){
                        res = "OK";
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else if(method.equals("del")){
                try {
                    int num = result-1;
                    System.out.println(num+"+++++++++++++++++++++++++++++");
                    conn = DriverManager.getConnection(url,user,password);
                    String sql = "UPDATE postinfo SET numNeeded="+num +" WHERE uid="+uid+";";
                    System.out.println(sql);
                    statement = conn.createStatement();
                    if (statement.executeUpdate(sql)>0){
                        res = "OK";
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
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
        return res;
    }


}
