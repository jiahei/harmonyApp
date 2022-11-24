package com.example.myspring.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class response {
    private String msg;
    private Integer code;
    private Object result;
}
