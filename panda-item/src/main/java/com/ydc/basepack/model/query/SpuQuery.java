package com.ydc.basepack.model.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ydc.basepack.model.dto.SpuDTO;
import com.ydc.basepack.model.entity.Spu;
import lombok.Data;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
public class SpuQuery extends Page<SpuDTO> {
    private String name;// 名称
    private Boolean saleable;// 是否上架
    //todo 根据分类查询较难实现
    //private String categoryName; // 商品分类名称拼接
    private String brandName;// 品牌名称
//
//    /**
//     * 方便同时获取3级分类
//     * @return
//     */
//    @JsonIgnore
//    //todo 这里通过@JsonIgnore注解来忽略这个getCategoryIds方法，避免将其序列化到JSON结果
//    public List<Long> getCategoryIds(){
//        return Arrays.asList(cid1, cid2, cid3);
//    }
}
