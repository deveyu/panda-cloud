package com.ydc.basepack.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydc.basepack.mapper.BrandMapper;
import com.ydc.basepack.model.entity.Brand;
import com.ydc.basepack.model.query.BrandQuery;
import com.ydc.basepack.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpectGroupServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {

    @Autowired
    private BrandMapper brandMapper;
    @Override
    public BrandQuery pageBrandByQuery(BrandQuery query) {

        query.setDesc("create_time","update_time");
        brandMapper.pageBrandByQuery(query);
        return query;
    }
}
