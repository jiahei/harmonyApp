package com.example.myharmonyapplication_api4.beans;

public class InfoResult {

    //定义接收到的数据中有的三个数据变量
    private String msg;
    private Integer code;
    private Object result;

    //构造函数
    public InfoResult(String msg, Integer code, Object result) {
        this.msg = msg;
        this.code = code;
        this.result = result;
    }

    public InfoResult() {
    }

    @Override
    public String toString() {
        return "InfoResult{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", result=" + result +
                '}';
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
