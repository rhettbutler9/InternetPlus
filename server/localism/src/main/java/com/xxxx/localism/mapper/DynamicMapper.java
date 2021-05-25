package com.xxxx.localism.mapper;

import com.xxxx.localism.pojo.Dynamic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2021-05-24
 */
public interface DynamicMapper extends BaseMapper<Dynamic> {

    List<Dynamic> getAllDynamicInfo(@Param("currentPage") Integer currentPage, @Param("size") Integer size, @Param("str") String str);
}
