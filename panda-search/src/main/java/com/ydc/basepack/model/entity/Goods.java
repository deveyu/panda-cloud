package com.ydc.basepack.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Map;
import java.util.Set;

@Data
@Document(indexName = "commodity", type = "docs", shards = 1, replicas = 1)
public class Goods {
    /**
     * 作为索引主键
     */
    @Id
    @Field(type = FieldType.Keyword)
    private Long id; // spuId
    @Field(type = FieldType.Keyword, index = false)
    private String subTitle;
    /**
     * 用于页面展示的sku信息，因为不参与搜索，所以转为json存储。然后设置不索引，不搜索。包含skuId、image、price、title字段
     */
    @Field(type = FieldType.Keyword, index = false)
    private String skus;// sku信息的json结构

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String all; // 所有需要被搜索的信息，包含标题，分类，甚至品牌
    private Long brandId;// 品牌id
    private Long categoryId;// 商品3级分类id
    private Long createTime;// spu创建时间
    /**
     * 价格数组，是所有sku的价格集合。方便根据价格进行筛选过滤
     */
    private Set<Long> price;// 价格
    /**
     * 所有规格参数的集合。key是参数名，值是参数值
     */
    private Map<String, Object> specs;// 可搜索的规格参数，key是参数名，值是参数值
}