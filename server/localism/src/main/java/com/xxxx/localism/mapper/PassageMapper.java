package com.xxxx.localism.mapper;

import com.xxxx.localism.pojo.Passage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2021-05-20
 */
public interface PassageMapper extends BaseMapper<Passage> {


    List<Passage> getAllPassageByMenuId(@Param("id") Integer id, @Param("currentPage") Integer currentPage, @Param("size") Integer size, @Param("how") String how);

    Passage getSinglePassageInfo(Integer id);

    Boolean getStar(@Param("id") Integer id, @Param("adminId") Integer adminId);

    Boolean getCollect(@Param("id") Integer id, @Param("adminId") Integer adminId);


    /**
     * 上传文件
     * @param url
     * @param adminId
     * @param menuId
     * @param t1
     * @param t2
     * @param time
     * @param imageUrl
     * @param name
     * @return
     */
    int uploadFile(@Param("url") String url, @Param("adminId") Integer adminId, @Param("menuId") Integer menuId,
                   @Param("t1") String t1, @Param("t2") String t2, @Param("time") Date time, @Param("imageUrl") String imageUrl, @Param("name") String name);
}
