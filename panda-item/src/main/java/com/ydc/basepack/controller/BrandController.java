package com.ydc.basepack.controller;


import com.ydc.basepack.model.query.BrandQuery;
import com.ydc.basepack.service.BrandService;
import com.yukong.panda.common.util.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.PastOrPresent;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("brand")
@Api(value = "品牌controller", tags = {"品牌操作接口"})
public class BrandController {
    private static final String MODULE_NAME = "商品模块";

    @Autowired
    private BrandService brandService;


    @ApiOperation(value = "品牌信息分页查询", notes = "品牌信息分页查询", httpMethod = "GET")
    @ApiImplicitParam(name = "brandQuery", value = "品牌信息查询类", required = false, dataType = "brandQuery")
    @GetMapping("page")
    public ApiResult<BrandQuery> pageByQuery(BrandQuery brandQuery){
        return new ApiResult<>(brandService.pageBrandByQuery(brandQuery));
    }


    @ApiOperation(value = "品牌商标上传", notes = "品牌商标上传", httpMethod = "POST")
    @ApiImplicitParam(name = "imageUpload", value = "品牌商标上传", required = false, dataType = "imageUpload")
    @PostMapping("uploadImage")
    public ApiResult<String> uploadImage(@RequestParam("file") MultipartFile file, HttpServletRequest req) throws IOException {
        // 判断文件是否为空，空则返回失败页面
        if (file.isEmpty()) {
            return new ApiResult<>("上传失败");
        }
        // 获取文件存储路径（绝对路径）
        String path = req.getServletContext().getRealPath("/WEB-INF/file");
        // 获取原文件名
        String fileName = file.getOriginalFilename();
        // 创建文件实例
        File filePath = new File(path, fileName);
        // 如果文件目录不存在，创建目录
        if (!filePath.getParentFile().exists()) {
            filePath.getParentFile().mkdirs();
            System.out.println("创建目录" + filePath);
        }
        // 写入文件
        file.transferTo(filePath);

        return new ApiResult<>("上传成功");
    }

}
