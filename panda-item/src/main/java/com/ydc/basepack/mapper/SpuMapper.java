package com.ydc.basepack.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ydc.basepack.model.dto.SpuDTO;
import com.ydc.basepack.model.dto.SpuDetailDTO;
import com.ydc.basepack.model.entity.Category;
import com.ydc.basepack.model.entity.Spu;
import com.ydc.basepack.model.query.SpuQuery;
import com.yukong.panda.common.base.mapper.BaseMapper;

import java.util.List;

public interface SpuMapper extends BaseMapper<Spu> {


    SpuQuery pageByQuery(SpuQuery query);

    SpuDetailDTO querySpuDetailById(Long id);

}
