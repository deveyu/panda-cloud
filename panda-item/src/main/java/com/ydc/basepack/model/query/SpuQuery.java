package com.ydc.basepack.model.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ydc.basepack.model.dto.SpuDTO;
import com.ydc.basepack.model.entity.Spu;
import com.yukong.panda.common.entity.SuperPage;
import lombok.Data;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * query类需要继承Page<entity/DTO>
 */
@Data
public class SpuQuery extends SuperPage<Spu> {
    private String name;// 名称
    private Boolean saleable;// 是否上架
    //todo 根据分类查询较难实现
    //private String categoryName; // 商品分类名称拼接
    private String brandName;// 品牌名称

}
