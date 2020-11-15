package com.ydc.basepack.model.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ydc.basepack.model.entity.Brand;
import lombok.Data;

import java.util.Date;

@Data
public class BrandQuery extends Page<Brand> {
    private String name;
    private String letter;
    private String delFlag;


}
