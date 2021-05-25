package com.xxxx.localism.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author xxx
 * @since 2021-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_video")
@ApiModel(value="Video对象", description="")
public class Video implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer adminId;

    @ApiModelProperty(value = "视频名字")
    private String name;

    @ApiModelProperty(value = "首页图片")
    private String image;

    private String content;

    @ApiModelProperty(value = "点赞数")
    private Integer stars;

    @ApiModelProperty(value = "是否点赞")
    @TableField(exist = false)
    private boolean star;


    @ApiModelProperty(value = "收藏数")
    private Integer collects;

    @ApiModelProperty(value = "是否收藏")
    @TableField(exist = false)
    private boolean collect;

    @ApiModelProperty(value = "播放数")
    private Integer play;

    @ApiModelProperty(value = "评论")
    @TableField(exist = false)
    private List<Comment> comments;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @TableField(exist = false)
    private Admin admin;

    @TableLogic
    private Integer deleted;

}
