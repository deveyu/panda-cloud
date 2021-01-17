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
import com.yukong.panda.common.constants.MqQueueNameConstant;
import com.yukong.panda.common.constants.RedisKey;
import com.yukong.panda.common.util.RedisUtil;
import jdk.nashorn.internal.ir.CallNode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**todo 带优化该查询时间接近2s
     * @return
     */
    @Override
    public List<CategoryTree> getCategoryByTree() {

        //todo
        List<CategoryTree> categoryTrees = (List<CategoryTree>) redisUtil.get(RedisKey.CATEGORY);
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


    @Override
    public Boolean addCategory(Category category) {
        category.setCreateTime(new Date());
        if (categoryMapper.insert(category) != 1) {
            return Boolean.FALSE;
        }
        syncCategory();

        return Boolean.TRUE;
    }


    @Override
    public Boolean updateCategory(Category category) {
        category.setUpdateTime(new Date());
        if (categoryMapper.updateById(category) != 1) {
            return Boolean.FALSE;
        }
        syncCategory();
        return Boolean.TRUE;
    }


    @Override
    public Boolean removeCategory(Long id) {

        if (categoryMapper.deleteById(id) != 1) {
            return Boolean.FALSE;
        }
        syncCategory();
        return Boolean.TRUE;
    }


    /**
     * 通知
     */
    private void sendMessage() {
        //todo 发送消息，使用feign调用rabbit-producer的接口？
        rabbitTemplate.convertAndSend(MqQueueNameConstant.ITEM_CATEGORY_QUEUE, "");
    }

    /**
     * 全量同步分类到redis
     */
    private void syncCategory() {
        //直接从缓存获取
        List<Category> categories = categoryMapper.queryAll();
        List<CategoryTree> categoryTrees = TreeUtil.list2Tree(categories, CommonConstants.CATEGORY_TREE_ROOT);
        redisUtil.set(RedisKey.CATEGORY, categoryTrees);
    }
}
