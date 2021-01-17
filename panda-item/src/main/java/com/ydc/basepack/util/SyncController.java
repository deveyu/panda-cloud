package com.ydc.basepack.util;


import com.ydc.basepack.mapper.CategoryMapper;
import com.ydc.basepack.model.dto.CategoryTree;
import com.ydc.basepack.model.entity.Category;
import com.yukong.panda.common.constants.CommonConstants;
import com.yukong.panda.common.constants.RedisKey;
import com.yukong.panda.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 方案一：手动将mysql全量同步到redis，即缓存预热
 * 方案二：数据量不大，项目启动时自动触发全量同步
 */
@RestController
@RequestMapping("item")
public class SyncController {
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("sync")
    public void sync(@RequestBody Map<String,Integer> map) {
        for (Map.Entry<String, Integer> m : map.entrySet()) {

            Integer value = m.getValue();
            if (value == 1) {
                switch (m.getKey()){
                    case "category":syncCategory();
                }
            }
        }
    }




    /**
     * 全量同步分类到redis
     */
    private  void syncCategory() {
        //直接从缓存获取
        List<Category> categories = categoryMapper.queryAll();
        List<CategoryTree> categoryTrees = TreeUtil.list2Tree(categories, CommonConstants.CATEGORY_TREE_ROOT);
        redisUtil.set(RedisKey.CATEGORY,categoryTrees);
    }



}
