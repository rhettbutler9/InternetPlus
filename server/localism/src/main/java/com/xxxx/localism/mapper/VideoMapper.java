package com.xxxx.localism.mapper;

import com.xxxx.localism.pojo.Video;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2021-05-17
 */
public interface VideoMapper extends BaseMapper<Video> {


    /**
     * 获取当前视频信息
     * @param id
     * @return
     */
    Video getCurrentVideoInfo(Integer id);



    Boolean getStar(@Param("id") Integer id, @Param("adminId") Integer adminId);

    Boolean getCollect(@Param("id") Integer id, @Param("adminId") Integer adminId);


    List<Video> getAllPassageByMenuId(@Param("id") Integer id, @Param("currentPage") Integer currentPage, @Param("size") Integer size, @Param("how") String how);
}
