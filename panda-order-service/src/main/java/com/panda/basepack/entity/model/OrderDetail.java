package com.panda.basepack.entity.model;

import lombok.Data;

import java.util.Date;

@Data
public class OrderDetail {
    private Long id;
    /**
     * 订单编号
     */
    private Long orderId;
    /**
     * 商品id
     */
    private Long skuId;
    /**
     * 商品购买数量
     */
    private Integer num;
    /**
     * 商品标题
     */
    private String title;
    /**
     * 商品单价
     */
    private Long price;
    /**
     * 商品规格数据
     */
    private String ownSpec;
    /**
     * 图片
     */
    private String image;
    private Date createTime;
    private Date updateTime;
}