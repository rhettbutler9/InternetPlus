package com.xxxx.localism.service;

import com.xxxx.localism.pojo.Dynamic;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.localism.pojo.RespBean;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xxx
 * @since 2021-05-24
 */
public interface IDynamicService extends IService<Dynamic> {

    /**
     * 上传图片
     * @param file
     * @return
     */
    RespBean uploadImage(MultipartFile file);

    /**
     * 发布动态
     * @param content
     * @param adminId
     * @param tags
     * @param url
     * @return
     */
    RespBean addDynamic(String content, Integer adminId, String tags, String[] url);

    /**
     * 获取所有动态信息
     * @param chooseWhat
     * @param currentPage
     * @param size
     * @return
     */
    RespBean getAllDynamicInfo(Integer chooseWhat, Integer currentPage, Integer size);
}
