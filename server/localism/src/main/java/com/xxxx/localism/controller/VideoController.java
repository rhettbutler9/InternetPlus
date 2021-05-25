package com.xxxx.localism.controller;


import com.xxxx.localism.pojo.RespBean;
import com.xxxx.localism.pojo.RespPageBean;
import com.xxxx.localism.service.IVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xxx
 * @since 2021-05-17
 */
@RestController
@Api(tags = "VideoController")
@RequestMapping("/api/v1")
public class VideoController {

    @Autowired
    private IVideoService videoService;

    @ApiOperation(value = "获取当前视频信息")
    @GetMapping("/video/currentInfo")
    public RespBean getCurrentVideoInfo(@RequestParam Integer id,
                                        @RequestParam Integer adminId){
        return videoService.getCurrentVideoInfo(id,adminId);
    }

    @ApiOperation(value = "获取所有视频通过菜单")
    @GetMapping("/video/allVideoInfo")
    public RespBean getAllVideoByMenuId(@RequestParam Integer id,
                                          @RequestParam(defaultValue = "1" )Integer currentPage,
                                          @RequestParam(defaultValue = "10")Integer size,
                                          @RequestParam Integer chooseWhat){
        //通过播放量
        String how="";
        if(chooseWhat==0){
            how="v.play";
        }else if(chooseWhat==1){ //通过创建时间
            how="v.create_time";
        }
        return videoService.getAllPassageByMenuId(id,currentPage,size,how);
    }

    @ApiOperation(value = "获取所有视频(分页+播放量排序)")
    @GetMapping("/video/play")
    public RespPageBean getAllVideoByPlay(@RequestParam(defaultValue = "1") Integer currentPage,
                                          @RequestParam(defaultValue = "10") Integer size){
        return videoService.getAllVideoByPlay(currentPage,size);
    }

    @ApiOperation(value = "获取所有视频(分页+创建时间排序)")
    @GetMapping("/video/ct")
    public RespPageBean getAllVideoByCT(@RequestParam(defaultValue = "1")Integer currentPage,
                                        @RequestParam(defaultValue = "10")Integer size){
        return videoService.getAllVideoByCT(currentPage,size);
    }


}

