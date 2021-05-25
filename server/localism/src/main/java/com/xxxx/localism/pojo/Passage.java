package com.xxxx.localism.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
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
 * @since 2021-05-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_passage")
@ApiModel(value="Passage对象", description="")
public class Passage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "发布人")
    private Integer adminId;

    private String content;
    
    private String name;

    private String image;

    private Integer play;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @TableField(exist = false)
    private Admin admin;

    private Integer stars;

    private Integer collects;

    @TableField(exist = false)
    private boolean isStar;

    @TableField(exist = false)
    private boolean isCollect;

    @TableField(exist = false)
    private List<Comment> comments;

    
    @TableLogic
    private Integer deleted;
}
