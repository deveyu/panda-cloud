package com.ydc.basepack.service;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ydc.basepack.feign.ItemClient;
import com.ydc.basepack.feign.model.*;
import com.ydc.basepack.model.entity.Goods;
import com.yukong.panda.common.util.ApiResult;
import com.yukong.panda.common.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private ItemClient itemClient;


    /**
     * ItemClient返回的最大实体为Spu,对应搜索服务的Goods
     */
    public Goods buildGoods(SpuDTO spuDTO) {
        //拼接all字段：即所有需要被搜索的字段
        //分类
        ApiResult<List<Category>> listApiResult = itemClient.queryCategoryBySpuId(spuDTO.getCategoryIds());
        List<Category> data = listApiResult.getData();
        String categoryNames = data.stream().map(Category::getName).collect(Collectors.joining(","));
        //品牌
        ApiResult<Brand> brandApiResult = itemClient.queryBrandById(spuDTO.getBrandId());
        Brand brand = brandApiResult.getData();

        String all = spuDTO.getName() + categoryNames + brand.getName();


        //sku集合
        ApiResult<List<SkuDTO>> listApiResult1 = itemClient.querySkuBySpuId(spuDTO.getId());
        List<SkuDTO> data1 = listApiResult1.getData();
        //因为不需要sku对象的全部字段，准备一个map承接
        List<Map<String, Object>> skuList = new ArrayList<>();
        for (SkuDTO skuDTO : data1) {
            Map<String, Object> map = new HashMap<>(1 << 4);
            map.put("id", skuDTO.getId());
            map.put("price", skuDTO.getPrice());
            map.put("title", skuDTO.getCreateTime());
            String images = skuDTO.getImages();
            map.put("image", images.substring(0, images.indexOf(",")));
            skuList.add(map);
        }

        // 3 当前spu下所有sku的价格的集合
        Set<Long> price = data1.stream().map(SkuDTO::getPrice).collect(Collectors.toSet());

        // 4 当前spu的规格参数
        Map<String, Object> specs = new HashMap<>(1 << 4);
        // 4.1 获取规格参数key，来自于SpecParam
        List<SpecParamDTO> specParams = itemClient.getSpecParamByCid(spuDTO.getCid3()).getData();
        // 4.2 获取规格参数的值，来自于spuDetail
        SpuDetailDTO spuDetail = itemClient.querySpuDetailById(spuDTO.getId()).getData();
        // 4.2.1 通用规格参数值
        Map<Long, Object> genericSpec = JsonUtils.toMap(spuDetail.getGenericSpec(), Long.class, Object.class);
        // 4.2.2 特有规格参数值
        Map<Long, List<String>> specialSpec = JsonUtils.nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>() {
        });

        for (SpecParamDTO specParam : specParams) {
            // 获取规格参数的名称
            String key = specParam.getName();
            // 获取规格参数值
            Object value = null;
            // 判断是否是通用规格
            if (specParam.getGeneric()) {
                // 通用规格
                value = genericSpec.get(specParam.getId());
            } else {
                // 特有规格
                value = specialSpec.get(specParam.getId());
            }
            // 判断是否是数值类型
            if (specParam.getNumeric()) {
                // 是数字类型，分段
                value = chooseSegment(value, specParam);
            }
            // 添加到specs
            specs.put(key, value);
        }


        Goods goods = new Goods();
        // 从spu对象中拷贝与goods对象中属性名一致的属性
        goods.setBrandId(spuDTO.getBrandId());
        goods.setCategoryId(spuDTO.getCid3());
        goods.setId(spuDTO.getId());
        goods.setSubTitle(spuDTO.getSubTitle());
        goods.setCreateTime(spuDTO.getCreateTime().getTime());
        goods.setSkus(JsonUtils.toString(skuList));
        goods.setSpecs(specs);
        goods.setPrice(price);
        goods.setAll(all);


        return goods;
    }

    private String chooseSegment(Object value, SpecParamDTO p) {
        if (value == null || StringUtils.isBlank(value.toString())) {
            return "其它";
        }
        double val = parseDouble(value.toString());
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = parseDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = parseDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    private double parseDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return 0;
        }
    }

}
