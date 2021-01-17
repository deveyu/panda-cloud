package com.ydc.basepack.start;

import com.ydc.basepack.mapper.CategoryMapper;
import com.ydc.basepack.model.dto.CategoryTree;
import com.ydc.basepack.model.entity.Category;
import com.ydc.basepack.util.TreeUtil;
import com.yukong.panda.common.constants.CommonConstants;
import com.yukong.panda.common.constants.RedisKey;
import com.yukong.panda.common.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 实现ApplicationRunner接口,执行顺序按照value值决定,值小先执行
 */
@Component
@Order(value = 1)
@Slf4j
public class SyncAfterStart implements ApplicationRunner {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        syncCategory();

    }
    /**
     * 全量同步分类到redis
     */
    private  void syncCategory() {

        //直接从缓存获取
        List<Category> categories = categoryMapper.queryAll();
        List<CategoryTree> categoryTrees = TreeUtil.list2Tree(categories, CommonConstants.CATEGORY_TREE_ROOT);
        redisUtil.set(RedisKey.CATEGORY,categoryTrees);
        log.info("商品分类同步redis成功");
    }


}
