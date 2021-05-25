package com.xxxx.localism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxxx.localism.mapper.ReplyMapper;
import com.xxxx.localism.pojo.*;
import com.xxxx.localism.mapper.VideoMapper;
import com.xxxx.localism.service.IVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xxx
 * @since 2021-05-17
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements IVideoService {

    @Resource
    private VideoMapper videoMapper;
    @Resource
    private ReplyMapper replyMapper;

    /**
     * 获取当前视频信息
     * @param id
     * @return
     */

    @Override
    public RespBean getCurrentVideoInfo(Integer id,Integer adminId) {
        Video video= new Video();
        video=videoMapper.getCurrentVideoInfo(id);
        Boolean isStar=videoMapper.getStar(id,adminId);
        Boolean isCollect=videoMapper.getCollect(id,adminId);
        if(isStar)
            video.setStar(isStar);
        if(isCollect)
            video.setCollect(isCollect);
        if(video.getId()!=null){
            return RespBean.success("获取成功!",video);
        }
        return RespBean.error("获取失败");
    }

    /**
     * 获取所有视频(分页+播放量排序)
     * @param currentPage
     * @param size
     * @return
     */
    @Override
    public RespPageBean getAllVideoByPlay(Integer currentPage, Integer size) {

        Page<Video> page=new Page<>(currentPage,size);
        Page<Video> videoPage = videoMapper.selectPage(page, new QueryWrapper<Video>().orderByDesc("play"));
        RespPageBean respPageBean=new RespPageBean(videoPage.getTotal(),videoPage.getRecords());
        return respPageBean;
    }

    /**
     * 获取所有视频(分页+创建时间排序)
     * @param currentPage
     * @param size
     * @return
     */
    @Override
    public RespPageBean getAllVideoByCT(Integer currentPage, Integer size) {
        Page<Video> page=new Page<>(currentPage,size);
        Page<Video> videoPage = videoMapper.selectPage(page, new QueryWrapper<Video>().orderByAsc("create_time"));
        RespPageBean respPageBean=new RespPageBean(videoPage.getTotal(),videoPage.getRecords());
        return respPageBean;
    }

    @Override
    public RespBean getAllPassageByMenuId(Integer id, Integer currentPage, Integer size, String how) {
        List<Video> list=videoMapper.getAllPassageByMenuId(id,(currentPage-1)*size,size,how);
        if(list.size()!=0){
            return RespBean.success("获取成功",list);
        }
        return RespBean.error("获取失败");
    }
}
