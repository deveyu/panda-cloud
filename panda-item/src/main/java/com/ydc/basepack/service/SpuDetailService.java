package com.ydc.basepack.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ydc.basepack.model.dto.SpuDetailDTO;
import com.ydc.basepack.model.entity.SpecGroup;
import com.ydc.basepack.model.entity.SpuDetail;
import com.ydc.basepack.model.query.SpecGroupQuery;

/**
 * @author pc
 */
public interface SpuDetailService extends IService<SpuDetail> {

    SpuDetailDTO getSpuDetailById(Long id);
}