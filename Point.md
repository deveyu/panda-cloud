**记录一些疑问**

```
SysDict

@TableField(value = "`desc`")
```
在Springboot启动器的web包下包含了javax.validation.Valid用于校验参数
@Null 限制只能为null
@NotNull 限制必须不为null
@AssertFalse 限制必须为false
@AssertTrue 限制必须为true
@DecimalMax(value) 限制必须为一个不大于指定值的数字
@DecimalMin(value) 限制必须为一个不小于指定值的数字
@Digits(integer,fraction) 限制必须为一个小数，且整数部分的位数不能超过integer，小数部分的位数不能超过fraction
@Future 限制必须是一个将来的日期
@Max(value) 限制必须为一个不大于指定值的数字
@Min(value) 限制必须为一个不小于指定值的数字
@Past 限制必须是一个过去的日期
@Pattern(value) 限制必须符合指定的正则表达式
@Size(max,min) 限制字符长度必须在min到max之间
@Past 验证注解的元素值（日期类型）比当前时间早
@NotEmpty 验证注解的元素值不为null且不为空（字符串长度不为0、集合大小不为0）
@NotBlank 验证注解的元素值不为空（不为null、去除首位空格后长度为0），不同于@NotEmpty，@NotBlank只应用于字符串且在比较时会去除字符串的空格
@Email 验证注解的元素值是Email，也可以通过正则表达式和flag指定自定义的email格式

作者：叫我胖虎大人
链接：https://www.jianshu.com/p/c8686fa5ef63
来源：简书
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。



不同分类的商品,比如手机和汽车，其规格参数不一样，规格组也不一样，规格参数值更不一样
同一分类的商品，比如都是手机，其规格参数名称是一致的，规格参数值不一样。

商品的规格参数与商品分类绑定
商品的规格参数值与商品绑定

因此：
- 规格参数的名称（key）与值（value）应该分开来保存（现实生活中的key-value往往要拆分用不同表存储！）


但是这样规格参数太多，衍生出规格参数组的概念

商品的规格参数组与商品分类绑定
规格参数与规格参数组和商品分类都绑定，多对一

规格参数的设计非常复杂：要考虑规格参数的单位，规格参数还会作为搜索服务的过滤条件
考虑该规格参数是否是iphone12通用的规格参数，用一个字段generic字段区分，0对应spu,1对应sku

下面考虑规格参数值的存储设计

华为Mate10 就是一个商品集（SPU）
因为颜色、内存等不同，而细分出不同的Mate10，如亮黑色128G版。（SKU）

spu的设计较为简单：无非就是从所有商品的属性抽出共同的
id:主键
title：标题
description：描述
specification：规格
packaging_list：包装
after_service：售后服务
comment：评价
category_id：商品分类
brand_id：品牌


另外我们手机分类下所有共通的规格参数值也保存在spu(detail)中，保存方式为json,key为规格参数key,value为规格参数值
这个表的字段比较多需要垂直拆分。拆分表为spu_detail

但是sku的设计与spec_param一样复杂
iphone12的不同内存的手机价钱不同，不同颜色图片不同，保存方式为json,key为规格参数key,value为规格参数值



总结：对于万事万物，描述他们的属性是不同的，属性值也不同。
其实很多东西都要按照这样去设计





### 搜索模块的思考

索引库同步数据库数据（网达使用定时任务扫库的方式，会有延迟）

#### A确定将哪些字段存入索引库

1需要展示的字段

2需要被搜索/过滤的字段，多个字段可以拼接在在一个字段中

#### B确定数据结构，将预想结果以json形式写出

#### C编写实体类映射索引库，确定存储类型等

#### D建立索引库与类型

#### E导入数据

