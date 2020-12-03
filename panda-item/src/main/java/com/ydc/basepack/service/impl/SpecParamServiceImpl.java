package com.ydc.basepack.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydc.basepack.mapper.SpecGroupMapper;
import com.ydc.basepack.mapper.SpecParamMapper;
import com.ydc.basepack.model.dto.SpecParamDTO;
import com.ydc.basepack.model.entity.SpecGroup;
import com.ydc.basepack.model.entity.SpecParam;
import com.ydc.basepack.model.query.SpecGroupQuery;
import com.ydc.basepack.model.query.SpecParamQuery;
import com.ydc.basepack.service.SpecGroupService;
import com.ydc.basepack.service.SpecParamService;
import com.yukong.panda.common.util.BeanHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecParamServiceImpl extends ServiceImpl<SpecParamMapper, SpecParam> implements SpecParamService {

    @Autowired
    private SpecParamMapper specParamMapper;


    @Override
    public List<SpecParamDTO> getSpecParamByGid(Long gid) {
        QueryWrapper<SpecParam> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SpecParam::getGroupId, gid);
        List<SpecParam> specParams = specParamMapper.selectList(wrapper);
        List<SpecParamDTO> specParamDTOS = BeanHelper.copyWithCollection(specParams, SpecParamDTO.class);
        return specParamDTOS;
    }

    @Override
    public List<SpecParamDTO> getSpecParamByCid(Long cid) {
        QueryWrapper<SpecParam> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SpecParam::getCid, cid);
        List<SpecParam> specParams = specParamMapper.selectList(wrapper);
        List<SpecParamDTO> specParamDTOS = BeanHelper.copyWithCollection(specParams, SpecParamDTO.class);
        return specParamDTOS;
    }
}
