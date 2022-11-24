package com.example.myharmonyapplication_api4.utils;

import ohos.app.Context;
import ohos.net.NetHandle;
import ohos.net.NetManager;
import ohos.net.NetStatusCallback;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpRequestUtils {

    private static String sendRequest(Context context,String urlString,String method,String token,String data){
        //定义返回的结果为null
        String result =null;
        //定义NetManager变量，看是否有网络通信
        NetManager netManager = NetManager.getInstance(context);
        //如果没有网络结束通信
        if (!netManager.hasDefaultNet()) {
            return null;
        }
        NetHandle netHandle = netManager.getDefaultNet();
        // 可以获取网络状态的变化
        NetStatusCallback callback = new NetStatusCallback() {
        };
        netManager.addDefaultNetStatusCallback(callback);
        // 通过openConnection来获取URLConnection
        HttpURLConnection connection = null;
        try {
            //定义请求的url
            URL url = new URL(urlString);
            URLConnection urlConnection = netHandle.openConnection(url,
                    java.net.Proxy.NO_PROXY);
            connection = (HttpURLConnection) urlConnection;
            //设置请求方式
            connection.setRequestMethod(method);
            //如果通过请求体传递参数到服务端接口，需要对connection进行额外设置
            if (data !=null){
                //允许通过此网络连接向服务端写入数据
                connection.setDoOutput(true);
                connection.setRequestProperty("Content_Type","application/json;charset=utf-8");
            }
            //如果token！=null,则需要将token设置到请求头中
            if (token!=null){
                connection.setRequestProperty("token",token);
            }
            //发送请求建立连接
            connection.connect();
            //向服务端传递数据
            if (data !=null ){
                byte[] bytes = data.getBytes("UTF-8");
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(bytes);
                outputStream.flush();
                outputStream.close();
            }
            // 之后可进行url的其他操作
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream inputStream = connection.getInputStream();
                StringBuilder stringBuilder = new StringBuilder();
                byte[] bytes = new byte[1024];
                int len=-1;
                while ((len=inputStream.read(bytes))!=-1){
                    stringBuilder.append(new String(bytes,0,len));
                }
                result = stringBuilder.toString();
                System.out.println("------------------------------------"+result);
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null){
                connection.disconnect();
            }
        }
        return result;
    }

    //向服务器发送GET请求
    public static String sendGetRequest(Context context,String urlString){
        return sendRequest(context,urlString,"GET",null,null);
    }

    //向服务器发送POST请求
    public static String sendPostRequest(Context context,String urlString){
        return sendRequest(context,urlString,"POST",null,null);
    }

}
