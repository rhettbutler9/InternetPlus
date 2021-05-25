package com.xxxx.localism.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;

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
 * @since 2021-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_dynamic")
@ApiModel(value="Dynamic对象", description="")
public class Dynamic implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "发布人")
    private Integer adminId;

    private String content;

    private String image;

    private Integer play;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private Integer stars;

    private Integer collects;

    private String  tags;
    @TableLogic
    private Boolean deleted;

    @TableField(exist = false)
    private Admin admin;

    @TableField(exist = false)
    private String [] url;
}
