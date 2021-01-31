package com.ydc.basepack;

import com.ydc.basepack.feign.ItemClient;
import com.ydc.basepack.feign.model.SpuDTO;
import com.ydc.basepack.feign.model.SpuQuery;
import com.ydc.basepack.model.entity.Goods;
import com.ydc.basepack.repository.GoodsRepository;
import com.ydc.basepack.service.SearchService;
import com.yukong.panda.common.util.ApiResult;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
public class SyncController {


    @Autowired
    private SearchService searchService;

    @Autowired
    private GoodsRepository repository;

    @Autowired
    private ItemClient itemClient;


    @GetMapping("sync")
    public void loadData() {
        int page = 1, rows = 100, size = 0;
        do {
            try {
                SpuQuery spuQuery = new SpuQuery();
                spuQuery.setCurrent(page);
                spuQuery.setSize(rows);
                // 查询spu
                ApiResult<SpuQuery> spuQueryApiResult = itemClient.queryByPage(spuQuery);
                // 取出spu
                List<SpuDTO> items = spuQueryApiResult.getData().getRecords();
                // 转换
                List<Goods> goodsList = items
                        .stream().map(searchService::buildGoods)
                        .collect(Collectors.toList());

                repository.saveAll(goodsList);
                page++;
                size = items.size();
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        } while (size == 100);
    }

}
