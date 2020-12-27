package com.ydc.basepack.service;

import com.ydc.basepack.feign.ItemClient;
import com.ydc.basepack.feign.model.Brand;
import com.ydc.basepack.feign.model.Category;
import com.ydc.basepack.feign.model.SkuDTO;
import com.ydc.basepack.feign.model.SpuDTO;
import com.ydc.basepack.model.entity.Goods;
import com.yukong.panda.common.util.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private ItemClient itemClient;


    /**
     * ItemClient返回的最大实体为Spu,对应搜索服务的Goods
     */
    public Goods buildGoods(SpuDTO spuDTO){
        //拼接all字段：即所有需要被搜索的字段
        //分类
        ApiResult<List<Category>> listApiResult = itemClient.queryCategoryBySpuId(spuDTO.getCategoryIds());
        List<Category> data = listApiResult.getData();
        String categoryNames = data.stream().map(Category::getName).collect(Collectors.joining(","));

        //品牌
        ApiResult<Brand> brandApiResult = itemClient.queryBrandById(spuDTO.getBrandId());
        Brand brand = brandApiResult.getData();

        String all=spuDTO.getName()+categoryNames+brand.getName();



        //sku集合
        ApiResult<List<SkuDTO>> listApiResult1 = itemClient.querySkuBySpuId(spuDTO.getId());
        List<SkuDTO> data1 = listApiResult1.getData();
        //因为不需要sku对象的全部字段，准备一个map承接
        List<Map<String, Object>> skuList = new ArrayList<>();
        for (SkuDTO skuDTO : data1) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", skuDTO.getId());
            map.put("price", skuDTO.getPrice());
            map.put("title", skuDTO.getCreateTime());
            String images = skuDTO.getImages();
            map.put("image", images.substring(0,images.indexOf(",")));
            skuList.add(map);
        }




        return goods;
    }


}
