package com.ydc.basepack.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ydc.basepack.model.entity.Category;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data

public class BrandDTO {
    private Long id;
    private List<Category> categoryList;
    private String name;
    private String image;
    private String letter;
    private List<List<Long>> cids;  // [[1,2,3],[34,35,53]]
}
