package com.ydc.basepack.feign.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;


/**
 * 此处是将spu做了垂直拆分
 * spu与spuDetail是一对一的关系
 */
@Data
@Accessors(chain = true)//chain 若为true，则setter方法返回当前对象
@TableName("tb_spu_detail")
public class SpuDetail {
    private static final long serialVersionUID = 1L;
    @TableId(value = "spu_id", type = IdType.AUTO)
    private Long spuId;// 对应的SPU的id
    private String description;// 商品描述
    private String specialSpec;// 商品特殊规格的名称及可选值模板
    private String genericSpec;// 商品的全局规格属性
    private String packingList;// 包装清单
    private String afterService;// 售后服务
    private Date createTime;// 创建时间
    private Date updateTime;// 最后修改时间
}
