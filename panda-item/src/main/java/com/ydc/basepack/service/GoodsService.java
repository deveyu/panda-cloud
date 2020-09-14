package com.ydc.basepack.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ydc.basepack.model.dto.SpuDTO;
import com.ydc.basepack.model.entity.Brand;
import com.ydc.basepack.model.entity.Spu;
import com.ydc.basepack.model.query.BrandQuery;

public interface GoodsService extends IService<Spu> {

  SpuDTO queryByPage(SpuDTO spuDTO);
}
