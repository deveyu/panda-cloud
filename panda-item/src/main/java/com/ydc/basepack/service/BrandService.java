package com.ydc.basepack.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ydc.basepack.model.entity.Brand;
import com.ydc.basepack.model.query.BrandQuery;
import com.yukong.panda.common.entity.SysUser;

import java.util.List;

public interface BrandService extends IService<Brand> {


  BrandQuery pageByQuery(BrandQuery brandQuery);
}
