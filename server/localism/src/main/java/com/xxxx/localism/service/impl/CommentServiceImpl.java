package com.xxxx.localism.service.impl;

import com.xxxx.localism.mapper.ReplyMapper;
import com.xxxx.localism.pojo.Comment;
import com.xxxx.localism.mapper.CommentMapper;
import com.xxxx.localism.pojo.RespBean;
import com.xxxx.localism.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xxx
 * @since 2021-05-17
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Resource
    private CommentMapper commentMapper;
    @Resource
    private ReplyMapper replyMapper;


    /**
     *
     * 添加评论
     * @param videoId
     * @param commentId
     * @param comment
     * @param chooseWhat
     * @return
     */
    @Override
    public RespBean addComment(Integer videoId, Integer commentId, Comment comment, Integer chooseWhat) {
        String table1="";
        String table2="";
        String table3="";
        String table4="";
        String id="";
        if(chooseWhat==0){
            table1="t_comment";
            table2="comment_video";
            id="vid";
            table3="t_reply";
            table4="comment_reply";
        }else {
            table1="p_comment";
            table2="p_comment_passage";
            id="pid";
            table3="p_reply";
            table4="p_comment_reply";
        }
        comment.setCreateTime(new Date());
        comment.setModifiedTime(new Date());
        Integer result=0;
        if(commentId==0){
            result=commentMapper.addComment(comment.getAdminId(),comment.getContent(),
                                            comment.getCreateTime(),videoId,
                                            table1,table2,id);
        }
        if(commentId==0){
            comment.setContent("");
            comment.setAdminId(0);
        }
        result = replyMapper.addComment(commentId, comment.getAdminId(),
                comment.getContent(), comment.getCreateTime(),
                table1,table3,table4);
        if (result == 1) {
            return RespBean.success("评论成功!");
        }
        return RespBean.error("评论失败");
    }


    public void test(){
        List<Integer> list=commentMapper.test();
        replyMapper.deleteList(list);
        System.out.println(list);
    }

    @Override
    public RespBean deleteComment(Integer videoId, Integer replyId, Integer commentId, String table1, String table2, String table3, String table4, String id) {
        Integer result=0;
        List<Integer> list=new ArrayList<>();
        if(replyId==0){
            list=commentMapper.getReply(commentId);
            System.out.println(list);
            commentMapper.deleteComment(table1,table2,id,videoId,commentId);
            result=replyMapper.deleteReply(list,table3,table4,commentId);
        }
        else {
            list.add(replyId);
            result=replyMapper.deleteReply(list,table3,table4,commentId);
        }
        if(result==1){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }


}
