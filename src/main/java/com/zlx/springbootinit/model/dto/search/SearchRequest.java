package com.zlx.springbootinit.model.dto.search;

import com.zlx.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author zlx
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchRequest extends PageRequest implements Serializable {


    /**
     * 搜索词
     */
    private String searchText;

    private static final long serialVersionUID = 1L;
}