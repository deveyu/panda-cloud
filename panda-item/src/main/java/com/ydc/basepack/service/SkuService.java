package com.ydc.basepack.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ydc.basepack.model.dto.SkuDTO;
import com.ydc.basepack.model.dto.SpuDetailDTO;
import com.ydc.basepack.model.entity.Sku;
import com.ydc.basepack.model.entity.SpuDetail;

import java.util.List;

/**
 * @author pc
 */
public interface SkuService extends IService<Sku> {

    List<SkuDTO> getSkuBySpuId(Long spuId);
}