
package com.yukong.panda.common.entity;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;


import java.util.Collections;
import java.util.List;

/**
 * copy mybatis-plus
 * 增强返回值类型
 *
 * @author ydc
 * @since 2018-06-09
 */
@Data
public class SuperPage<T> extends Page<T> {

    private static final long serialVersionUID = 8545996863226528798L;

    /**
     * 查询数据列表
     */
    private List<?> sRecords = Collections.emptyList();

}
