package com.xxxx.localism.controller;

import com.xxxx.localism.pojo.Admin;
import com.xxxx.localism.pojo.RespBean;
import com.xxxx.localism.pojo.RespPageBean;
import com.xxxx.localism.service.IAdminService;
import com.xxxx.localism.utils.FastDFSUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Map;

@RestController
@Api(tags = "AdminController")
@RequestMapping("/api/v1")
public class AdminController {

    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "获取信息")
    @GetMapping("/admin/info")
    public Admin getAdminInfo(Principal principal){
        if(null==principal)
            return null;
        String username=principal.getName();
        Admin admin=adminService.getAdminByUserName(username);
        admin.setPassword(null);
        return admin;
    }

    @ApiOperation(value = "获取信息通过id")
    @GetMapping("/admin/info/{id}")
    public RespBean getAdminInfoById(@PathVariable Integer id){
        return adminService.getAdminInfoById(id);
    }

    @ApiOperation(value = "更新用户信息")
    @PutMapping("/admin/info")
    public RespBean updateAdminInfo(@RequestBody Admin admin, Authentication authentication){
        if(adminService.updateById(admin)){
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(admin,
                    null,authentication.getAuthorities()));
            return RespBean.success("更新成功!");
        }
        return RespBean.error("更新失败!");
    }


    @ApiOperation(value = "更新密码")
    @PutMapping("/admin/password")
    public RespBean updateAdminPassword(@RequestBody Map<String,Object> info){
        String oldPass = (String) info.get("oldPass");
        String password = (String) info.get("password");
        Integer adminId = (Integer) info.get("adminId");
        return adminService.updateAdminPassword(oldPass,password,adminId);
    }

    @ApiOperation(value = "获取当前最大id")
    @GetMapping("/maxid")
    public RespBean maxId(){
        return adminService.maxId();
    }

    @ApiOperation(value = "获取所有用户(分页)")
    @GetMapping("/admin/all")
    public RespPageBean getAllAdminInfo(@RequestParam(defaultValue = "1") Integer currentPage,
                                        @RequestParam(defaultValue = "10") Integer size){
        return adminService.getAllAdminInfo(currentPage,size);
    }

    @ApiOperation(value = "通过token获取用户id")
    @GetMapping("/user/{token}")
    public Object getPartyId(@PathVariable String token){
        return adminService.getPartyId(token);
    }


    @ApiOperation(value = "获取用户所在房间")
    @GetMapping("/party/{user_id}")
    public Object getAdminRoom(@PathVariable Integer user_id){
        return adminService.getAdminRoom(user_id);
    }


    @ApiOperation(value = "点赞")
    @PostMapping("/admin/star/addvideo")
    public RespBean addStar(@RequestParam Integer Id,
                            @RequestParam Integer adminId,
                            @RequestParam Integer chooseWhat){
        String table1="";
        String table2="";
        String id="";
        if(chooseWhat==0){
            table1="star";
            table2="t_video";
            id="vid";
        }else {
            table1="p_star";
            table2="t_passage";
            id="pid";
        }
        return adminService.addStar(Id,adminId,table1,table2,id);
    }


    @ApiOperation(value = "取消点赞")
    @DeleteMapping("/admin/star/deletevideo")
    public RespBean deleteStar(@RequestParam Integer videoId,
                               @RequestParam Integer adminId,
                               @RequestParam Integer chooseWhat) {
        String table1="";
        String table2="";
        String id="";
        if(chooseWhat==0){
            table1="star";
            table2="t_video";
            id="vid";
        }else {
            table1="p_star";
            table2="t_passage";
            id="pid";
        }
        return adminService.deleteStar(videoId,adminId,table1,table2,id);
    }

    @ApiOperation(value = "收藏")
    @PostMapping("/admin/collect")
    public RespBean addCollect(@RequestParam Integer videoId,
                               @RequestParam Integer adminId,
                               @RequestParam Integer chooseWhat){
        String table1="";
        String table2="";
        String id="";
        if(chooseWhat==0){
            table1="collect";
            table2="t_video";
            id="vid";
        }else {
            table1="p_collect";
            table2="t_passage";
            id="pid";
        }
        return adminService.addCollect(videoId,adminId,table1,table2,id);
    }

    @ApiOperation(value = "取消收藏")
    @DeleteMapping("/admin/collect")
    public RespBean deleteCollect(@RequestParam Integer videoId,
                                  @RequestParam Integer adminId,
                                  @RequestParam Integer chooseWhat){
        String table1="";
        String table2="";
        String id="";
        if(chooseWhat==0){
            table1="collect";
            table2="t_video";
            id="vid";
        }else {
            table1="p_collect";
            table2="t_passage";
            id="pid";
        }
        return adminService.deleteCollect(videoId,adminId,table1,table2,id);
    }

    @ApiOperation(value = "更新用户头像")
    @PostMapping("/admin/upload/userface")
    public RespBean uploadUserFace(MultipartFile file, Integer id,Authentication authentication){
        String[] filePath = FastDFSUtils.upload(file);
        String url = FastDFSUtils.getTrackerUrl() + filePath[0] + "/" + filePath[1];
        return  adminService.uploadUserFace(url,id,authentication);
    }

    @ApiOperation(value = "上传文件")
    @PostMapping("/admin/upload/file")
    public RespBean uploadFile(@RequestParam MultipartFile file,
                               @RequestParam Integer adminId,
                               @RequestParam Integer chooseWhat,
                               @RequestParam Integer menuId,
                               @RequestParam MultipartFile image,
                               @RequestParam String  name){
        String[] filePath = FastDFSUtils.upload(file);
        String fileUrl = FastDFSUtils.getTrackerUrl() + filePath[0] + "/" + filePath[1];
        String imageUrl="";
        if(!image.isEmpty()){
            String[] imagePath = FastDFSUtils.upload(image);
            imageUrl = FastDFSUtils.getTrackerUrl() + imagePath[0] + "/" + imagePath[1];
        }
        return adminService.uploadFile(fileUrl,adminId,chooseWhat,menuId,imageUrl,name);
    }

    @ApiOperation(value = "删除文件")
    @DeleteMapping("admin/delete/file")
    public RespBean deleteFile(@RequestParam Integer adminId,
                               @RequestParam Integer id,
                               @RequestParam Integer chooseWhat){
        return adminService.deleteFile(adminId,id,chooseWhat);
    }


}
