package com.ydc.basepack.controller;


import cn.hutool.extra.ftp.Ftp;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    @Resource
    private Ftp ftpClientUtil;


    @ApiOperation(value = "品牌信息分页查询", notes = "品牌信息分页查询", httpMethod = "GET")
    @ApiImplicitParam(name = "brandQuery", value = "品牌信息查询类", required = false, dataType = "brandQuery")
    @GetMapping("page")
    public ApiResult<BrandQuery> pageByQuery(BrandQuery brandQuery) {
        return new ApiResult<>(brandService.pageBrandByQuery(brandQuery));
    }


    @ApiOperation(value = "品牌商标上传", notes = "品牌商标上传", httpMethod = "POST")
    @ApiImplicitParam(name = "imageUpload", value = "品牌商标上传", required = false, dataType = "imageUpload")
    @PostMapping("uploadImage")
    public ApiResult<String> uploadImage(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest req) throws IOException {
        // 判断文件是否为空，空则返回失败页面
        if (multipartFile.isEmpty()) {
            return new ApiResult<>("上传文件不能为空");
        }

        //System.getProperty("user.dir");参数即可获得项目相对路径。
        //获得项目的类路径
        String path = ResourceUtils.getURL("classpath:").getPath();
        //todo 空文件夹在编译时不会打包进入target中
        File uploadDir = new File(path + "/static/image/brand/");
        if (!uploadDir.exists()) {
            log.info("上传路径不存在，正在创建...");
            if (uploadDir.mkdirs()) {//注意是mkdirs创建多级目录
                log.info("上传文件夹创建成功");
            }
        }
        File avatar = null;
        //获得上传文件的文件名
        String oldName = multipartFile.getOriginalFilename();
        log.info("[上传的文件名]：" + oldName);

        avatar= new File(path + "static/image/brand/", oldName);
        //保存图片
        String nginxPath= null;
        try {
            multipartFile.transferTo(avatar);
            //todo 图片放到该目录下不能访问,应该和nginx配置有关
            //nginxPath = "http://119.45.104.69/ftp/root/brand/"+oldName;//没有该文件夹会自动创建
            nginxPath = "http://119.45.104.69/ftp/root/brand/"+oldName;//没有该文件夹会自动创建
            ftpClientUtil.upload("/brand", avatar);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }finally {
            //todo 为啥关闭了ftpUtil，ftpUtil下次使用时为空 ;还会报421
            //ftpClientUtil.close();
        }
        return new ApiResult<>(nginxPath, "上传成功");


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
