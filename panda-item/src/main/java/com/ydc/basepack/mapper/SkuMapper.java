package com.ydc.basepack.mapper;

import com.ydc.basepack.model.dto.SkuDTO;
import com.ydc.basepack.model.dto.SpuDetailDTO;
import com.ydc.basepack.model.entity.Sku;
import com.ydc.basepack.model.entity.Spu;
import com.ydc.basepack.model.query.SpuQuery;
import com.yukong.panda.common.base.mapper.BaseMapper;

import java.util.List;

public interface SkuMapper extends BaseMapper<Sku> {


    List<SkuDTO> querySkuBySpuId(Long id);
}
