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
 * @since 2021-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_comment")
@ApiModel(value="Comment对象", description="")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    private Integer adminId;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "回复")
    @TableField(exist = false)
    private    List<Reply> children;

    @TableLogic
    private Boolean deleted;


}
