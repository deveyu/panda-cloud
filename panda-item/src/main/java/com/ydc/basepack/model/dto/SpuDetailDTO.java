package com.ydc.basepack.model.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ydc.basepack.model.entity.Spu;
import com.ydc.basepack.model.entity.SpuDetail;
import lombok.Data;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
public class SpuDetailDTO extends Page<SpuDetail> {
    private Long spuId;// 对应的SPU的id
    private String description;// 商品描述
    private String specialSpec;// 商品特殊规格的名称及可选值模板
    private String genericSpec;// 商品的全局规格属性
    private String packingList;// 包装清单
    private String afterService;// 售后服务
    private Date createTime;// 创建时间
    private Date updateTime;// 最后修改时间

}