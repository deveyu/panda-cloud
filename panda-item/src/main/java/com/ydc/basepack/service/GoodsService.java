package com.ydc.basepack.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ydc.basepack.model.dto.SkuDTO;
import com.ydc.basepack.model.dto.SpuDTO;
import com.ydc.basepack.model.dto.SpuDetailDTO;
import com.ydc.basepack.model.entity.Brand;
import com.ydc.basepack.model.entity.Spu;
import com.ydc.basepack.model.query.BrandQuery;
import com.ydc.basepack.model.query.SpuQuery;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface GoodsService extends IService<Spu> {

    SpuQuery queryByPage(SpuQuery spuQuery);

    SpuDetailDTO querySpuDetailById(@PathVariable("spuId") Long id);

    Boolean upAndDownSpu(Spu spu);
}
