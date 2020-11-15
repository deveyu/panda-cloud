package com.ydc.basepack.service.impl;

import cn.hutool.db.DaoTemplate;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydc.basepack.mapper.BrandMapper;
import com.ydc.basepack.model.entity.Brand;
import com.ydc.basepack.model.query.BrandQuery;
import com.ydc.basepack.service.BrandService;
import com.yukong.panda.common.enums.DataStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {

    @Autowired
    private BrandMapper brandMapper;
    @Override
    public BrandQuery pageBrandByQuery(BrandQuery query) {
        query.setDelFlag(DataStatusEnum.NORMAL.getCode());
        query.setDesc("create_time","update_time");
        brandMapper.pageBrandByQuery(query);
        return query;
    }

    @Override
    public Boolean deleteById(Integer id) {
        //逻辑删除
        Brand brand = super.getById(id);
        brand.setDelFlag(DataStatusEnum.LOCK.getCode());
        super.updateById(brand);
        return Boolean.TRUE;
    }
}
