package com.ydc.basepack.model.dto;

import com.ydc.basepack.model.entity.Category;
import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class CategoryTree {
    private Long id;
    private String label;
    private Long parentId;
    private Integer isParent;
    private Integer sort;
    private Date updateTime;
    private Date createTime;

    private List<CategoryTree> children;

    public void addChildren(CategoryTree tree) {
        this.children.add(tree);
    }
}
