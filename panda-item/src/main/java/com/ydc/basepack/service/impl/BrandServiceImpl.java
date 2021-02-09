package com.ydc.basepack.service.impl;

import cn.hutool.db.DaoTemplate;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydc.basepack.mapper.BrandMapper;
import com.ydc.basepack.model.dto.BrandDTO;
import com.ydc.basepack.model.entity.Brand;
import com.ydc.basepack.model.query.BrandQuery;
import com.ydc.basepack.service.BrandService;
import com.yukong.panda.common.constants.RedisKey;
import com.yukong.panda.common.enums.DataStatusEnum;
import com.yukong.panda.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private RestTemplate restTemplate;

    // inject the template as ZSetOperations
    @Resource(name = "redisTemplate")
    private ZSetOperations<String, Long> zsetOps;

    @Resource(name = "redisTemplate")
    private HashOperations<String, Long, Brand> hashOps;

    @Override
    public BrandQuery pageBrandByQuery(BrandQuery query) {
        query.setDelFlag(DataStatusEnum.NORMAL.getCode());

        getBrandFromRedis(query);
        //query.setDesc("create_time","update_time");
        brandMapper.pageBrandByQuery(query);
        return query;
    }


    //存取去缓存
    private List<Brand> getBrandFromRedis(BrandQuery query) {
        long current = query.getCurrent();
        long size = query.getSize();
        //redis从0开始，
        long start = (current - 1) * size;
        long end = start + size - 1;
        //按照下标获取
        Set<Long> brandIdSet = zsetOps.reverseRangeByScore(RedisKey.BRAND_ZSET, start, end);
        if (brandIdSet == null) {
            IPage<Brand> brandIPage = brandMapper.pageBrandByQuery(query);
            List<Brand> records = brandIPage.getRecords();

            for (Brand record : records) {
                zsetOps.add(RedisKey.BRAND_ZSET, record.getId(),Double.parseDouble(String.valueOf(record.getCreateTime().getTime())));
            }
        }
        List<Brand> brands = hashOps.multiGet(RedisKey.BRAND_MAP, brandIdSet);
        return brands;
    }


    @Override
    public Boolean deleteById(Integer id) {
        //逻辑删除
        Brand brand = super.getById(id);
        brand.setDelFlag(DataStatusEnum.LOCK.getCode());
        super.updateById(brand);
        return Boolean.TRUE;
    }

    @Override
    public Brand queryBrandById(Long id) {
        return brandMapper.selectById(id);
    }
}
