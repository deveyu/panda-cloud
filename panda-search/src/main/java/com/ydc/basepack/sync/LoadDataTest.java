package com.ydc.basepack.sync;

import cn.hutool.db.PageResult;
import com.ydc.basepack.feign.ItemClient;
import com.ydc.basepack.feign.model.SpuDTO;
import com.ydc.basepack.feign.model.SpuQuery;
import com.ydc.basepack.model.entity.Goods;
import com.ydc.basepack.repository.GoodsRepository;
import com.ydc.basepack.service.SearchService;
import com.yukong.panda.common.util.ApiResult;
import org.junit.Test;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;


@RunWith(SpringRunner.class)
@SpringBootTest
public class LoadDataTest {

}