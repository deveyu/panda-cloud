package com.ydc.basepack.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydc.basepack.mapper.SpecGroupMapper;
import com.ydc.basepack.mapper.SpuDetailMapper;
import com.ydc.basepack.model.dto.SpuDetailDTO;
import com.ydc.basepack.model.entity.SpecGroup;
import com.ydc.basepack.model.entity.SpuDetail;
import com.ydc.basepack.model.query.SpecGroupQuery;
import com.ydc.basepack.service.SpecGroupService;
import com.ydc.basepack.service.SpuDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SpuDetailServiceImpl extends ServiceImpl<SpuDetailMapper, SpuDetail> implements SpuDetailService {

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Override
    public SpuDetailDTO getSpuDetailById(Long id) {
        SpuDetailDTO spuDetailDTO = new SpuDetailDTO();
        BeanUtils.copyProperties(spuDetailMapper.selectById(id),spuDetailDTO);
        return spuDetailDTO;
    }
}
