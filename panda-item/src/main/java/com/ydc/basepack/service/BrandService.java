package com.ydc.basepack.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ydc.basepack.model.entity.Brand;
import com.ydc.basepack.model.query.BrandQuery;

public interface BrandService extends IService<Brand> {

  BrandQuery pageBrandByQuery(BrandQuery brandVoQuery);
}
