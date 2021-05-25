package com.xxxx.localism.controller;


import com.xxxx.localism.pojo.RespBean;
import com.xxxx.localism.pojo.RespPageBean;
import com.xxxx.localism.service.IPassageService;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xxx
 * @since 2021-05-20
 */
@RestController
@RequestMapping("/api/v1")
public class PassageController {

    @Autowired
    private IPassageService passageService;

    @ApiOperation(value = "获取所有文章信息(通过菜单)")
    @GetMapping("/passage/allPassageInfo")
    public RespBean getAllPassageByMenuId(@RequestParam Integer id,
                                          @RequestParam(defaultValue = "1" )Integer currentPage,
                                          @RequestParam(defaultValue = "10")Integer size,
                                          @RequestParam Integer chooseWhat){
        //通过播放量
        String how="";
        if(chooseWhat==0){
            how="pplay";
        }else if(chooseWhat==1){ //通过创建时间
            how="pcreate_time";
        }
        return passageService.getAllPassageByMenuId(id,currentPage,size,how);
    }



    @ApiOperation(value = "获取单个文章的详细信息")
    @GetMapping("/passage/currentInfo/")
    public RespBean getSinglePassageInfo(@RequestParam Integer id,
                                         @RequestParam Integer adminId){
        return passageService.getSinglePassageInfo(id,adminId);
    }
}

