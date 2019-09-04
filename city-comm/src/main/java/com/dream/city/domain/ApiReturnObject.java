package com.dream.city.domain;

import lombok.Data;

@Data
public class ApiReturnObject {
    private Integer code;
    private String msg;

    ApiReturnObject(){
        this.code = 0;
        this.msg = "";
    }

    public ApiReturnObject(String msg){
        this.msg = msg;
    }

    ApiReturnObject(Integer code){
        this.code = code;
    }
}
