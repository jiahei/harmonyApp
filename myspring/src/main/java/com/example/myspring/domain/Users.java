package com.example.myspring.domain;

import lombok.Data;

@Data
public class Users {
    private Integer uid;
    private String username;
    private String password;

    public Users(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
