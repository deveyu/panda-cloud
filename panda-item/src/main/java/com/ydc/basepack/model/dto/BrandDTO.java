package com.ydc.basepack.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data

public class BrandDTO {
    private Long id;
    private String name;
    private String image;
    private String letter;
}
