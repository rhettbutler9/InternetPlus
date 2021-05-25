package com.xxxx.localism.service;

import com.xxxx.localism.pojo.Passage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.localism.pojo.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xxx
 * @since 2021-05-20
 */
public interface IPassageService extends IService<Passage> {


    /**
     * 获取所有文章信息
     * @param id
     * @param currentPage
     * @param size
     * @param how
     * @return
     */
    RespBean getAllPassageByMenuId(Integer id, Integer currentPage, Integer size, String how);

    /**
     * 获取单个文章的详细信息
     * @param id
     * @return
     */
    RespBean getSinglePassageInfo(Integer id,Integer adminId);
}
