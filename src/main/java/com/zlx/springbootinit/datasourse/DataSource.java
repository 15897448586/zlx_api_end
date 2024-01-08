package com.zlx.springbootinit.datasourse;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 数据源接口，导入数据必须实现
 * @param <T>
 */
public interface DataSource<T> {
    /**
     * 搜索
     * @param searchText
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<T> doSearch(String searchText, long pageNum, long pageSize);
}
