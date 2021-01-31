package com.ydc.basepack.feign;

import com.ydc.basepack.feign.model.*;
import com.yukong.panda.common.util.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ItemFallback implements ItemClient{
    @Override
    public ApiResult<List<SkuDTO>> querySkuBySpuId(Long id) {
        log.error("调用querySkuBySpuId方法异常，参数：{}", id);
        return null;
    }

    @Override
    public ApiResult<List<Category>> queryCategoryBySpuId(List<Long> ids) {
        log.error("调用queryCategoryBySpuId方法异常，参数：{}", ids);
        return null;
    }

    @Override
    public ApiResult<Brand> queryBrandById(Long id) {
        log.error("调用queryBrandById方法异常，参数：{}", id);
        return null;
    }

    @Override
    public ApiResult<List<SpecParamDTO>> getSpecParamByCid(Long cid) {
        log.error("调用getSpecParamByCid方法异常，参数：{}", cid);
        return null;
    }

    @Override
    public ApiResult<SpuQuery> queryByPage(SpuQuery spuQuery) {
        log.error("调用queryByPage方法异常，参数：{}", spuQuery);
        return null;
    }

    @Override
    public ApiResult<SpuDetailDTO> querySpuDetailById(Long id) {
        log.error("调用querySpuDetailById方法异常，参数：{}", id);
        return null;
    }
}
