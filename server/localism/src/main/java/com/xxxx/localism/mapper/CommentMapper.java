package com.xxxx.localism.mapper;

import com.xxxx.localism.pojo.Comment;
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
public interface CommentMapper extends BaseMapper<Comment> {

    List<Comment> getAllComment(Integer id);






    Integer addComment(@Param("adminId") Integer adminId, @Param("content") String content, @Param("createTime") Date createTime, @Param("videoId") Integer videoId, @Param("table1") String table1, @Param("table2") String table2, @Param("id") String id);

    Integer selectMaxId();

    List<Integer> test();

    /**
     *
     * @param commentId
     * @return
     */
    List<Integer> getReply(@Param("commentId") Integer commentId);

    Integer deleteComment(@Param("table1") String table1, @Param("table2") String table2,
                          @Param("id") String id, @Param("integer") Integer integer,
                          @Param("commentId") Integer commentId);

    /**
     * 添加评论
     * @param comment
     * @param commentId
     * @param adminId
     * @return
     */

}
