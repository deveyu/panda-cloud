package com.ydc.basepack.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydc.basepack.mapper.CategoryMapper;
import com.ydc.basepack.mapper.SpuMapper;
import com.ydc.basepack.model.dto.CategoryTree;
import com.ydc.basepack.model.dto.SpuDTO;
import com.ydc.basepack.model.entity.Category;
import com.ydc.basepack.model.entity.Spu;
import com.ydc.basepack.service.CategoryService;
import com.ydc.basepack.service.GoodsService;
import com.ydc.basepack.util.TreeUtil;
import com.yukong.panda.common.constants.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsServiceImpl extends ServiceImpl<SpuMapper, Spu> implements GoodsService {

    @Autowired
    private SpuMapper spuMapper;


    @Override
    public SpuDTO queryByPage(SpuDTO spuDTO) {
        spuMapper.pageByQuery(spuDTO);
        return spuDTO;
    }
}
