package com.xxxx.localism.service;

import com.xxxx.localism.pojo.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.localism.pojo.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xxx
 * @since 2021-05-17
 */
public interface ICommentService extends IService<Comment> {

    /**
     *
     * 添加视频评论
     * @param videoId
     * @param commentId
     * @param comment
     * @param chooseWhat
     * @return
     */
    RespBean addComment(Integer videoId, Integer commentId, Comment comment, Integer chooseWhat);


    void test();

    /**
     * 删除评论
     *
     * @param videoId
     * @param replyId
     * @param commentId
     * @param table1
     * @param table2
     * @param table3
     * @param table4
     * @param id
     * @return
     */
    RespBean deleteComment(Integer videoId, Integer replyId, Integer commentId, String table1, String table2, String table3, String table4, String id);

}
