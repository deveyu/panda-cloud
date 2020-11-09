package com.yukong.panda.user.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据字典
 * @author ydc
 * @date 2019-01-23 10:39
 */
@Data
//@Accessors用于配置getter和setter方法的生成结果
@Accessors(chain = true)//chain的中文含义是链式的，设置为true，则setter方法返回当前对象。
public class SysDict implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 描述
     */
    @TableField(value = "`desc`")
    private String desc;

    /**
     * 值
     */
    private String value;

    /**
     * 父级id  顶层默认-1
     */
    private Integer parentId;

    /**
     * 排序权重
     */
    private Integer sortOrder;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime modifyTime;

    /**
     * 是否删除 1-删除，0-未删除
     */
    private String delFlag;

}
