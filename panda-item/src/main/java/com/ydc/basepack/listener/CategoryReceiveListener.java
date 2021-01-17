//package com.ydc.basepack.listener;
//
//import com.rabbitmq.client.Channel;
//import com.ydc.basepack.mapper.CategoryMapper;
//import com.ydc.basepack.model.dto.CategoryTree;
//import com.ydc.basepack.model.entity.Category;
//import com.ydc.basepack.util.TreeUtil;
//import com.yukong.panda.common.constants.CommonConstants;
//import com.yukong.panda.common.constants.MqQueueNameConstant;
//import com.yukong.panda.common.constants.RedisKey;
//import com.yukong.panda.common.dto.SysLogDTO;
//import com.yukong.panda.common.util.RedisUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.List;
//
///**
//
// */
//@Slf4j
//@Component
//@RabbitListener(queues = MqQueueNameConstant.ITEM_CATEGORY_QUEUE)
//public class CategoryReceiveListener {
//
//    @Autowired
//    private RedisUtil redisUtil;
//    @Autowired
//    private CategoryMapper categoryMapper;
//
//
//    /**
//     * 更新缓存
//     * @param channel
//     * @param message
//     */
//    @RabbitHandler
//    public void handler( Channel channel, Message message) throws IOException {
//        //直接从缓存获取
//        List<Category> categories = categoryMapper.queryAll();
//        List<CategoryTree> categoryTrees = TreeUtil.list2Tree(categories, CommonConstants.CATEGORY_TREE_ROOT);
//        redisUtil.set(RedisKey.CATEGORY,categoryTrees);
//    }
//
//
//}
