package com.xxxx.localism.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.localism.pojo.Admin;
import com.xxxx.localism.pojo.AdminLoginParam;
import com.xxxx.localism.pojo.RespBean;
import com.xxxx.localism.pojo.RespPageBean;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface IAdminService extends IService<Admin> {


    RespBean login(String username, String password, String code,HttpServletRequest request);

    Admin getAdminByUserName(String username);

    /**
     * 注册
     * @param adminLoginParam
     * @param
     * @return
     */
    RespBean registerAdmin(AdminLoginParam adminLoginParam);

    /**
     * 获取当前最大id
     * @return
     */
    RespBean maxId();

    /**
     * 更新用户密码
     * @param oldPass
     * @param password
     * @param adminId
     * @return
     */
    RespBean updateAdminPassword(String oldPass, String password, Integer adminId);

    /**
     * 获取所有用户(分页)
     * @param currentPage
     * @param size
     * @return
     */
    RespPageBean getAllAdminInfo(Integer currentPage, Integer size);

    /**
     * 获取用户所在party
     * @param token
     * @return
     */
    Object getPartyId(String token);

    /**
     * 获取用户所在房间
     * @param user_id
     * @return
     */
    Object getAdminRoom(Integer user_id);

    /**
     * 获取信息通过id
     * @param id
     */
    RespBean getAdminInfoById(Integer id);

    /**
     * 点赞
     * @param Id
     * @param adminId
     * @param table1
     * @param table2
     * @param id
     * @return
     */
    RespBean addStar(Integer Id, Integer adminId, String table1, String table2, String id);

    /**
     * 取消点赞
     * @param videoId
     * @param adminId
     * @param table1
     * @param table2
     * @param id
     * @return
     */
    RespBean deleteStar(Integer videoId, Integer adminId, String table1, String table2, String id);

    /**
     * 收藏
     * @param videoId
     * @param adminId
     * @param table1
     * @param table2
     * @param id
     * @return
     */
    RespBean addCollect(Integer videoId, Integer adminId, String table1, String table2, String id);

    /**
     * 取消收藏
     * @param videoId
     * @param adminId
     * @param table1
     * @param table2
     * @param id
     * @return
     */
    RespBean deleteCollect(Integer videoId, Integer adminId, String table1, String table2, String id);

    /**
     * 更新用户头像
     * @param url
     * @param id
     * @param authentication
     * @return
     */
    RespBean uploadUserFace(String url, Integer id, Authentication authentication);

    /**
     * 上传文件
     * @param fileUrl
     * @param adminId
     * @param chooseWhat
     * @param menuId
     * @param imageUrl
     * @param name
     * @return
     */
    RespBean uploadFile(String fileUrl, Integer adminId, Integer chooseWhat, Integer menuId, String imageUrl, String name);

    /**
     * 删除文件
     * @param adminId
     * @param id
     * @param chooseWhat
     * @return
     */
    RespBean deleteFile(Integer adminId, Integer id, Integer chooseWhat);


    /**
     * 给图文点赞
     * @param videoId
     * @param adminId
     * @return
     */

}
