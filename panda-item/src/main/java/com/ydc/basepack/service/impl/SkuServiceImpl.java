package com.ydc.basepack.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydc.basepack.mapper.SkuMapper;
import com.ydc.basepack.mapper.SpecGroupMapper;
import com.ydc.basepack.model.entity.Sku;
import com.ydc.basepack.model.entity.SpecGroup;
import com.ydc.basepack.model.query.SpecGroupQuery;
import com.ydc.basepack.service.SkuService;
import com.ydc.basepack.service.SpecGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements SkuService {

    @Autowired
    private SkuMapper skuMapper;


    @Override
    public List<Sku> getSkuBySpuId(Long spuId) {
        return skuMapper.querySkuBySpuId(spuId);
    }
}
