package com.xxxx.localism.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AdminLoginParam {

    private String username;

    private String password;

    private String code;

}
