package com.ydc.basepack.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.regexp.internal.RE;
import com.ydc.basepack.mapper.BrandMapper;
import com.ydc.basepack.mapper.CategoryMapper;
import com.ydc.basepack.model.dto.CategoryTree;
import com.ydc.basepack.model.entity.Brand;
import com.ydc.basepack.model.entity.Category;
import com.ydc.basepack.model.query.BrandQuery;
import com.ydc.basepack.model.query.CategoryQuery;
import com.ydc.basepack.service.BrandService;
import com.ydc.basepack.service.CategoryService;
import com.ydc.basepack.util.TreeUtil;
import com.yukong.panda.common.constants.CommonConstants;
import com.yukong.panda.common.constants.RedisKey;
import com.yukong.panda.common.service.IRedisService;
import com.yukong.panda.common.service.RedisService;
import com.yukong.panda.common.util.BeanHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.integration.support.json.JacksonJsonUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private IRedisService redisService;
    @Autowired
    private RedisSerializer redisSerializer;

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
        //设置过期时间
        redisService.expire(RedisKey.CATEGORY_ID_MAP, 1,TimeUnit.DAYS);
        Map<Object, Object> categoryMap = redisService.hGetAll(RedisKey.CATEGORY_ID_MAP);

        assert categoryMap != null;
        if (!categoryMap.isEmpty()) {
            Set<Map.Entry<Object, Object>> entries = categoryMap.entrySet();
            for (Map.Entry<Object, Object> entry : entries) {
                Category category = (Category) redisSerializer.deserialize(entry.getValue().toString().getBytes());
                categories.add(category);
            }
        } else {
            QueryWrapper<Category> query = new QueryWrapper<>();
            categories = categoryMapper.queryAll();
            categories.forEach((category -> {
                byte[] bytes = redisSerializer.serialize(category);
                redisService.hSet(RedisKey.CATEGORY_ID_MAP, String.valueOf(category.getId()), bytes);
            }));
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
}
