package com.ydc.basepack.service;

import com.ydc.basepack.vo.ModifyVO;

import java.util.List;

/**
 * @author ydc
 * @description
 */
public interface ArticleService {
    void convert2Voice(String htmlText);
    List<ModifyVO> modifyArticle(String htmlText, Boolean async);
}
