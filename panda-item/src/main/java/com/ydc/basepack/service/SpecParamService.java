package com.ydc.basepack.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ydc.basepack.model.dto.SpecParamDTO;
import com.ydc.basepack.model.entity.SpecGroup;
import com.ydc.basepack.model.entity.SpecParam;
import com.ydc.basepack.model.query.SpecGroupQuery;
import com.ydc.basepack.model.query.SpecParamQuery;

import java.util.List;

/**
 * @author pc
 */
public interface SpecParamService extends IService<SpecParam> {


    List<SpecParamDTO> getSpecParamByGid(Long gid);
}