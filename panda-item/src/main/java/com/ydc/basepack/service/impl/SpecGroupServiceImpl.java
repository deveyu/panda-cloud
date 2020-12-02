package com.ydc.basepack.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydc.basepack.mapper.SpecGroupMapper;
import com.ydc.basepack.model.entity.SpecGroup;
import com.ydc.basepack.model.query.SpecGroupQuery;
import com.ydc.basepack.service.SpecGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SpecGroupServiceImpl extends ServiceImpl<SpecGroupMapper, SpecGroup> implements SpecGroupService {

    @Autowired
    private SpecGroupMapper SpecGroupMapper;


    @Override
    public SpecGroupQuery pageSpecGroupByCategory(SpecGroupQuery query) {
        SpecGroupMapper.pageByQuery(query);
        return query;
    }

    @Override
    public Boolean addSpecGroup(SpecGroup specGroup) {
        specGroup.setCreateTime(new Date());
        SpecGroupMapper.insert(specGroup);
        return Boolean.TRUE;
    }
}
