package com.xxxx.localism.controller;


import com.xxxx.localism.pojo.Admin;
import com.xxxx.localism.pojo.Dynamic;
import com.xxxx.localism.pojo.RespBean;
import com.xxxx.localism.service.IAdminService;
import com.xxxx.localism.service.IDynamicService;
import com.xxxx.localism.utils.FastDFSUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xxx
 * @since 2021-05-24
 */
@RestController
@RequestMapping("/api/v1")
public class DynamicController {

    @Autowired
    private IDynamicService dynamicService;
    @Autowired
    private IAdminService adminService;


    @ApiOperation(value = "发布动态")
    @PostMapping("/dynamic/add/dynamic")
    public RespBean addDynamic(@RequestParam String  content,
                               @RequestParam Integer adminId,
                               @RequestParam String  tags,
                               @RequestParam String []url){
        return dynamicService.addDynamic(content,adminId,tags,url);

    }

    @ApiOperation(value = "上传图片")
    @PostMapping("/dynamic/upload/image")
    public RespBean uploadImage(@RequestParam MultipartFile file){
        return dynamicService.uploadImage(file);
    }

    @ApiOperation(value = "获取所有动态信息")
    @GetMapping("/dynamic/getAllDynamicInfo")
    public RespBean getAllDynamicInfo(@RequestParam Integer chooseWhat,
                                      @RequestParam(defaultValue = "1") Integer currentPage,
                                      @RequestParam(defaultValue = "10") Integer size){
        return dynamicService.getAllDynamicInfo(chooseWhat,currentPage,size);
//        return null;
    }

    @ApiOperation(value = "获取动态的详细信息")
    @GetMapping("/dynamic/getSingleDynamicInfo")
    public  RespBean getSingleDynamicInfo(@RequestParam Integer id,
                                          @RequestParam(defaultValue = "1") Integer currentPage,
                                          @RequestParam(defaultValue = "10") Integer size){
//        return dynamicService.getSingleDynamicInfo(id,currentPage,size);
        Dynamic dynamic = dynamicService.getById(id);
        Admin admin = null;
        if(dynamic!=null){
            dynamic.setUrl(dynamic.getImage().split(","));
            dynamic.setImage("");
            admin=adminService.getById(dynamic.getAdminId());
            admin.setPassword("");
            dynamic.setAdmin(admin);
            return RespBean.success("获取成功",dynamic);
        }
        return RespBean.error("获取失败");
    }


}

