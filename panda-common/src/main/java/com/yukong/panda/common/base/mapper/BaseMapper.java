package com.yukong.panda.common.base.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @author yukong
 * @date 2019-01-23 10:14
 */
public interface BaseMapper<T> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {


    /**
     * 分页查询
     * @param query 分页查询条件
     * @return 分页查询结果
     */
    IPage<T> pageByQuery(Page<T> query);
    IPage<T> pageByQuery(Page<T> query, QueryWrapper<T> wrapper);

    /**
     * 分页查询总数
     * @param query 分页查询条件
     * @return 分页查询总数
     */
    Integer countByQuery(IPage<T> query);
}
