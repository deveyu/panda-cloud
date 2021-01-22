package com.ydc.basepack.controller;

import com.ydc.basepack.service.ArticleService;
import com.ydc.basepack.vo.ModifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ydc
 * @description
 */
@RestController
@RequestMapping("article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping("/convert")
    public Boolean convert2Voice(@RequestParam("htmlText") String htmlText) {
        articleService.convert2Voice(htmlText);
        return Boolean.TRUE;
    }

    @PostMapping("/modify")
    public List<ModifyVO> modify(@RequestParam("htmlText") String htmlText,@RequestParam("async")Boolean async) {
        List<ModifyVO> modifyVOS = articleService.modifyArticle(htmlText, async);
        return modifyVOS;
    }

    @PostMapping("/write")
    public String write(@RequestParam("userId") String userId,@RequestParam("nickname")String  nickname) {
       return articleService.getWriteUrl(userId, nickname);
    }
}
