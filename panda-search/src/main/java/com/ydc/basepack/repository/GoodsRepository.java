package com.ydc.basepack.repository;

import com.ydc.basepack.model.entity.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface GoodsRepository extends ElasticsearchRepository<Goods, Long> {


}