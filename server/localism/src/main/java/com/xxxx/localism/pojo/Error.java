package com.xxxx.localism.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Error {


    private boolean success;
    private String  err_msg;

    public static Error error(String msg){
        return new Error(false,msg);
    }

}
