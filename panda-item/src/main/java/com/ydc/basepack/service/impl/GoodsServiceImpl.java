package com.ydc.basepack.service.impl;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydc.basepack.mapper.CategoryMapper;
import com.ydc.basepack.mapper.SkuMapper;
import com.ydc.basepack.mapper.SpuDetailMapper;
import com.ydc.basepack.mapper.SpuMapper;
import com.ydc.basepack.model.dto.CategoryTree;
import com.ydc.basepack.model.dto.SkuDTO;
import com.ydc.basepack.model.dto.SpuDTO;
import com.ydc.basepack.model.dto.SpuDetailDTO;
import com.ydc.basepack.model.entity.*;
import com.ydc.basepack.model.query.SpuQuery;
import com.ydc.basepack.service.*;
import com.ydc.basepack.util.TreeUtil;
import com.yukong.panda.common.constants.CommonConstants;
import com.yukong.panda.common.util.BeanHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl extends ServiceImpl<SpuMapper, Spu> implements GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;






    @Override
    public SpuQuery queryByPage(SpuQuery spuQuery) {

        spuMapper.pageByQuery(spuQuery);
        List<Spu> records = spuQuery.getRecords();
        List<SpuDTO> dtoList = BeanHelper.copyWithCollection(records, SpuDTO.class);
        spuQuery.setSRecords(dtoList);

        //todo 获取返回结果后有查询两次数据库；这里与使用多表连接究竟哪个快？
        if (!CollectionUtils.isEmpty(dtoList)) {
            for (SpuDTO record : dtoList) {
                String categoryName = categoryService.listByIds(record.getCategoryIds())
                        .stream().map(Category::getName).collect(Collectors.joining("/"));
                record.setCategoryName(categoryName);
                Brand brand = brandService.getById(record.getBrandId());
                record.setBrandName(brand.getName());

                record.setSpuDetail(spuDetailMapper.selectById(record.getId()));
                record.setSkus(skuMapper.querySkuBySpuId(record.getId()));
            }
        }
        return spuQuery;
    }


    @Override
    public SpuDetailDTO querySpuDetailById(Long id) {
        SpuDetailDTO spuDetailDTO = new SpuDetailDTO();
        BeanUtils.copyProperties(spuDetailMapper.selectById(id),spuDetailDTO);
        return spuDetailDTO;
    }

    /**
     * 上下架
     * @return
     */
    @Override
    public Boolean upAndDownSpu(Spu spu) {
        UpdateWrapper<Spu> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",spu.getId());
        updateWrapper.set("saleable",spu.getSaleable());
        spuMapper.update(spu,updateWrapper);
        return spuMapper.updateById(spu) > 0;
    }
}
