package com.ydc.basepack.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.util.Date;

@TableName("tb_spec_group")
@Accessors(chain = true)//chain 若为true，则setter方法返回当前对象
@Data
public class SpecGroup {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long cid;

    private String name;

    private Date createTime;

    private Date updateTime;
}