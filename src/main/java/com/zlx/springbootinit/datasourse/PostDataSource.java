package com.zlx.springbootinit.datasourse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.zlx.springbootinit.common.ErrorCode;
import com.zlx.springbootinit.constant.CommonConstant;
import com.zlx.springbootinit.exception.BusinessException;
import com.zlx.springbootinit.exception.ThrowUtils;
import com.zlx.springbootinit.mapper.PostFavourMapper;
import com.zlx.springbootinit.mapper.PostMapper;
import com.zlx.springbootinit.mapper.PostThumbMapper;
import com.zlx.springbootinit.model.dto.post.PostEsDTO;
import com.zlx.springbootinit.model.dto.post.PostQueryRequest;
import com.zlx.springbootinit.model.entity.Post;
import com.zlx.springbootinit.model.entity.PostFavour;
import com.zlx.springbootinit.model.entity.PostThumb;
import com.zlx.api_comon.model.entity.User;
import com.zlx.springbootinit.model.vo.PostVO;
import com.zlx.springbootinit.model.vo.UserVO;
import com.zlx.springbootinit.service.PostService;
import com.zlx.springbootinit.service.UserService;
import com.zlx.springbootinit.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 帖子服务实现
 *
 * @author zlx
 */
@Service
@Slf4j
public class PostDataSource  implements DataSource<PostVO> {

    @Resource
    private PostService postService;

    @Override
    public Page<PostVO> doSearch(String searchText, long pageNum, long pageSize) {
        PostQueryRequest postQueryRequest = new PostQueryRequest();
        postQueryRequest.setSearchText(searchText);
        postQueryRequest.setCurrent(pageNum);
        postQueryRequest.setPageSize(pageSize);
        ServletRequestAttributes servletRequestAttributes =  (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();

        Page<PostVO> postVOPage = postService.listPostVOByPage(postQueryRequest, request);
        return postVOPage;
    }
}




