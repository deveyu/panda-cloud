package com.ydc.basepack.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.netflix.discovery.converters.Auto;
import com.ydc.basepack.mapper.CategoryMapper;
import com.ydc.basepack.model.dto.CategoryTree;
import com.ydc.basepack.model.entity.Category;
import com.ydc.basepack.model.query.CategoryQuery;
import com.ydc.basepack.service.CategoryService;
import com.ydc.basepack.util.TreeUtil;
import com.yukong.panda.common.constants.CommonConstants;
import com.yukong.panda.common.constants.RedisKey;
import com.yukong.panda.common.util.RedisUtil;
import jdk.nashorn.internal.ir.CallNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.data.redis.hash.ObjectHashMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    //todo 待优化

    /**
     * 数据库 18058 ms
     * 缓存 259 ms
     *
     * @return
     */
    @Override
    public List<CategoryTree> getAllCategory() {
        List<Category> categories = new ArrayList<>();
        //field:商品id;value:json
        //todo 操作redis时 K和HK都要是String
        BoundHashOperations<String,String,Category> boundHashOperations = redisTemplate.boundHashOps(RedisKey.CATEGORY_ID_MAP);
        boundHashOperations.expire(1, TimeUnit.DAYS);
        categories = boundHashOperations.values();
        assert categories != null;
        if (categories.isEmpty()) {
            categories = categoryMapper.queryAll();
            Map<String, Category> collect = categories.stream().collect(Collectors.toMap(category -> String.valueOf(category.getId()), category -> category, (key1, key2) -> key2));
            boundHashOperations.putAll(collect);
        }
        List<CategoryTree> categoryTrees = TreeUtil.list2Tree(categories, CommonConstants.CATEGORY_TREE_ROOT);
        return categoryTrees;
    }

    @Override
    public CategoryQuery getCategoryByPage(CategoryQuery query) {
        query.setAsc("sort");
        categoryMapper.getCategoryByPage(query);
        return query;
    }

    @Override
    public List<Category> queryCategoryBySpuId(List<Long> ids) {
        return categoryMapper.selectBatchIds(ids);
    }
}
