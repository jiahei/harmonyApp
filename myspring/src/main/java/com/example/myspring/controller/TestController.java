package com.example.myspring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

//Rest模式
@RestController
@RequestMapping("/Users")
@ResponseBody
public class TestController {

    @GetMapping
    public String getById(){
        System.out.println("springboot is running........");
        return "springboot is running........";
    }
}
