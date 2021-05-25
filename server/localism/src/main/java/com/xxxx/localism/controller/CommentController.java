package com.xxxx.localism.controller;


import com.xxxx.localism.pojo.Comment;
import com.xxxx.localism.pojo.RespBean;
import com.xxxx.localism.service.ICommentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xxx
 * @since 2021-05-17
 */
@RestController
@RequestMapping("/api/v1")
public class CommentController {

    @Autowired
    private ICommentService commentService;

    @ApiOperation(value = "添加评论")
    @PostMapping("/comment")
    public RespBean addComment(@RequestParam Integer videoId,
                                      @RequestParam Integer commentId,
                                      @RequestBody  Comment comment,
                                      @RequestParam Integer chooseWhat){
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
//        return  commentService.addComment(videoId,commentId,comment,table1,table2,id,table3,table4);
        return  commentService.addComment(videoId,commentId,comment,chooseWhat);
    }

    @ApiOperation(value = "删除评论")
    @DeleteMapping("/comment")
    public RespBean deleteComment(@RequestParam Integer videoId,
                                  @RequestParam Integer commentId,
                                  @RequestParam Integer replyId,
                                  @RequestParam Integer chooseWhat){
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
        return commentService.deleteComment(videoId,replyId,commentId,table1,table2,table3,table4,id);
    }

    @ApiOperation(value = "测试")
    @GetMapping("/aaa")
    public void test(){
        commentService.test();
    }


}

