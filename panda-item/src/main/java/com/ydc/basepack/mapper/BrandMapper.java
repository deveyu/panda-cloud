package com.ydc.basepack.mapper;

import cn.hutool.extra.ftp.FtpConfig;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ydc.basepack.model.entity.Brand;
import com.ydc.basepack.model.query.BrandQuery;
import com.yukong.panda.common.base.mapper.BaseMapper;

public interface BrandMapper extends BaseMapper<Brand> {

    IPage<Brand> pageBrandByQuery(BrandQuery query);
}
