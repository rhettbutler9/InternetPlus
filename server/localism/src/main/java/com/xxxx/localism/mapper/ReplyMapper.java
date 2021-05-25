package com.xxxx.localism.mapper;

import com.xxxx.localism.pojo.Reply;
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
 * @since 2021-05-17
 */
public interface ReplyMapper extends BaseMapper<Reply> {


    Integer addComment(@Param("commentId") Integer commentId, @Param("adminId") Integer adminId,
                       @Param("content") String content, @Param("createTime") Date createTime, @Param("table1") String table1, @Param("table3") String table3, @Param("table4") String table4);


    void deleteList(List<Integer> list);

    Integer deleteReply(@Param("list") List<Integer> list, @Param("table3") String table3, @Param("table4") String table4, @Param("commentId") Integer commentId);
}
