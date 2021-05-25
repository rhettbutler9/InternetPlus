package com.xxxx.localism.pojo;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Success {

    private boolean success;
    private Integer id;

    public static Success success(Integer id) {
        return new Success(true,id);
    }

}
