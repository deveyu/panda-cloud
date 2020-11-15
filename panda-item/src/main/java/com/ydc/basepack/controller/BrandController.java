package com.ydc.basepack.controller;


import cn.hutool.core.io.FileUtil;
import com.ydc.basepack.model.dto.BrandDTO;
import com.ydc.basepack.model.entity.Brand;
import com.ydc.basepack.model.query.BrandQuery;
import com.ydc.basepack.service.BrandService;
import com.yukong.panda.common.util.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.security.krb5.internal.crypto.Des;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.PastOrPresent;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("brand")
@Api(value = "品牌controller", tags = {"品牌操作接口"})
@Slf4j
public class BrandController {
    private static final String MODULE_NAME = "商品模块";

    @Autowired
    private BrandService brandService;


    @ApiOperation(value = "品牌信息分页查询", notes = "品牌信息分页查询", httpMethod = "GET")
    @ApiImplicitParam(name = "brandQuery", value = "品牌信息查询类", required = false, dataType = "brandQuery")
    @GetMapping("page")
    public ApiResult<BrandQuery> pageByQuery(BrandQuery brandQuery) {
        return new ApiResult<>(brandService.pageBrandByQuery(brandQuery));
    }


    @ApiOperation(value = "品牌商标上传", notes = "品牌商标上传", httpMethod = "POST")
    @ApiImplicitParam(name = "imageUpload", value = "品牌商标上传", required = false, dataType = "imageUpload")
    @PostMapping("uploadImage")
    public ApiResult<String> uploadImage(@RequestParam("file") MultipartFile uploadFile, HttpServletRequest req) throws IOException {
        // 判断文件是否为空，空则返回失败页面
        if (uploadFile.isEmpty()) {
            return new ApiResult<>("上传失败");
        }
        //获得项目的类路径
        String path = ResourceUtils.getURL("classpath:").getPath();
        //空文件夹在编译时不会打包进入target中
        File uploadDir = new File(path + "/static/image/brand/");
        if (!uploadDir.exists()) {
            log.info("上传路径不存在，正在创建...");
            if (uploadDir.mkdirs()) {//注意是mkdirs
                log.info("上传文件夹创建成功");
            }
        }
        File avatar=null;
        //获得上传文件的文件名
        try {
            String oldName = uploadFile.getOriginalFilename();
            log.info("[上传的文件名]：" + oldName);
            //我的文件保存在static目录下的avatar/user
             avatar = new File(path + "static/image/brand/", oldName);
            //保存图片
            uploadFile.transferTo(avatar);
        } catch (IOException e) {
            log.info("文件操作失败",e);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回成功结果，附带文件的相对路径  E:\IdeaProjects\panda-cloud\panda-item\target\classes\static\image\brand
        return new ApiResult<>(avatar.getAbsolutePath(), "上传成功");
    }


    @ApiOperation(value = "品牌添加", notes = "品牌添加", httpMethod = "POST")
    @ApiImplicitParam(name = "brandDTO", value = "品牌信息", required = false, dataType = "")
    @PostMapping
    public ApiResult<Boolean> saveBrand(@RequestBody BrandDTO brandDTO) {
        Brand brand = new Brand();
        BeanUtils.copyProperties(brandDTO, brand);
        return new ApiResult<>(brandService.save(brand));

    }


    @ApiOperation(value = "品牌修改", notes = "品牌修改", httpMethod = "PUT")
    @ApiImplicitParam(name = "brandDTO", value = "品牌信息", required = false, dataType = "")
    @PutMapping
    public ApiResult<Boolean> updateBrand(@RequestBody BrandDTO brandDTO) {
        Brand brand = new Brand();
        BeanUtils.copyProperties(brandDTO, brand);
        return new ApiResult<>(brandService.updateById(brand));
    }

    @ApiOperation(value = "品牌删除", notes = "品牌删除", httpMethod = "DELETE")
    @ApiImplicitParam(name = "id", value = "品牌id", required = true, dataType = "Integer")
    @DeleteMapping("id/{id}")
    public ApiResult<Boolean> deleteBrand(@PathVariable("id") Integer id) {
        return new ApiResult<>(brandService.deleteById(id));
    }

}
