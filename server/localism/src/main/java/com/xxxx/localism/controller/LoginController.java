package com.xxxx.localism.controller;

import com.xxxx.localism.pojo.Admin;
import com.xxxx.localism.pojo.AdminLoginParam;
import com.xxxx.localism.pojo.RespBean;
import com.xxxx.localism.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@Api(tags = "LoginController")
@RequestMapping("/api/v1")
public class LoginController {

    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public RespBean login(@RequestBody AdminLoginParam adminLoginParam, HttpServletRequest request){
        return  adminService.login(adminLoginParam.getUsername(),adminLoginParam.getPassword(),adminLoginParam.getCode(),request);
    }



    @ApiOperation(value = "注册")
    @PostMapping("/register")
    public RespBean registerAdmin(@RequestBody AdminLoginParam adminLoginParam){
        return adminService.registerAdmin(adminLoginParam);
    }



    @ApiOperation(value = "退出")
    @PostMapping("/logout")
    public  RespBean logout(){
        return RespBean.success("注销成功");
    }


}
