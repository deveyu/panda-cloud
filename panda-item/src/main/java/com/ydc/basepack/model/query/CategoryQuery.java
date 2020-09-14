package com.ydc.basepack.model.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ydc.basepack.model.entity.Category;
import lombok.Data;

@Data
public class CategoryQuery extends Page<Category> {
    private String name;


}
