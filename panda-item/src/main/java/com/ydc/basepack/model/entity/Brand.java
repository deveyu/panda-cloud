package com.ydc.basepack.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)//chain 若为true，则setter方法返回当前对象
@TableName("tb_brand")
public class Brand {


    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    private String image;
    private String letter;
    private Date createTime;
    private Date updateTime;

}
