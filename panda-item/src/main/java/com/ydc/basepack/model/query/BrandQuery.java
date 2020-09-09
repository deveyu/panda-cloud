package com.ydc.basepack.model.query;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ydc.basepack.model.entity.Brand;
import com.yukong.panda.common.vo.SysUserVo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
public class BrandQuery extends Page<Brand> {
    private String name;
    private String image;
    private String letter;
    private Date createTime;
    private Date updateTime;

}
