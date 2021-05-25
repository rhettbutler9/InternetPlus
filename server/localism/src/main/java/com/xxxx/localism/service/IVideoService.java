package com.xxxx.localism.service;

import com.xxxx.localism.pojo.RespBean;
import com.xxxx.localism.pojo.RespPageBean;
import com.xxxx.localism.pojo.Video;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xxx
 * @since 2021-05-17
 */
public interface IVideoService extends IService<Video> {

    /**
     * 获取当前视频信息
     *
     * @param id
     * @return
     */
    RespBean getCurrentVideoInfo(Integer id,Integer adminId);

    /**
     * 获取所有视频(分页+播放量排序)
     * @param currentPage
     * @param size
     * @return
     */
    RespPageBean getAllVideoByPlay(Integer currentPage, Integer size);

    /**
     * 获取所有视频(分页+创建时间排序)
     * @param currentPage
     * @param size
     * @return
     */
    RespPageBean getAllVideoByCT(Integer currentPage, Integer size);

    /**
     * 获取所有视频通过菜单
     * @param id
     * @param currentPage
     * @param size
     * @param how
     * @return
     */
    RespBean getAllPassageByMenuId(Integer id, Integer currentPage, Integer size, String how);
}
