package com.zlx.springbootinit.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlx.springbootinit.common.BaseResponse;
import com.zlx.springbootinit.common.ErrorCode;
import com.zlx.springbootinit.common.ResultUtils;
import com.zlx.springbootinit.datasourse.*;
import com.zlx.springbootinit.exception.BusinessException;
import com.zlx.springbootinit.exception.ThrowUtils;
import com.zlx.springbootinit.model.dto.post.PostQueryRequest;
import com.zlx.springbootinit.model.dto.search.SearchRequest;
import com.zlx.springbootinit.model.dto.user.UserQueryRequest;
import com.zlx.springbootinit.model.entity.Picture;
import com.zlx.springbootinit.model.enums.SearchTypeEnum;
import com.zlx.springbootinit.model.vo.PostVO;
import com.zlx.springbootinit.model.vo.SearchVO;
import com.zlx.springbootinit.model.vo.UserVO;
import com.zlx.springbootinit.service.PictureService;
import com.zlx.springbootinit.service.PostService;
import com.zlx.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class SearchFacede {

    @Resource
    private UserDataSource userDataSource;

    @Resource
    private PostDataSource postDataSource;

    @Resource
    private PictureDataSource pictureDataSource;

    @Resource
    private DataSourceRegistry dataSourceRegistry;

    public SearchVO searchAll(SearchRequest searchRequest, HttpServletRequest request) {
        String type = searchRequest.getType();
        SearchTypeEnum enumByValue = SearchTypeEnum.getEnumByValue(type);
        ThrowUtils.throwIf(StringUtils.isBlank(type), ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(enumByValue == null, ErrorCode.PARAMS_ERROR);
        String searchText = searchRequest.getSearchText();
        long current = searchRequest.getCurrent();
        long pageSize = searchRequest.getPageSize();
        if (enumByValue == null) {
            CompletableFuture<Page<Picture>> pictureTask = CompletableFuture.supplyAsync(() -> {
                return pictureDataSource.doSearch(searchText, current, pageSize);
            });
            CompletableFuture<Page<UserVO>> userTask = CompletableFuture.supplyAsync(() -> {
                UserQueryRequest userQueryRequest = new UserQueryRequest();
                userQueryRequest.setUserName(searchText);
                return userDataSource.doSearch(searchText, current, pageSize);
            });
            CompletableFuture<Page<PostVO>> postTask = CompletableFuture.supplyAsync(() -> {
                PostQueryRequest postQueryRequest = new PostQueryRequest();
                postQueryRequest.setSearchText(searchText);
                return postDataSource.doSearch(searchText, current, pageSize);
            });

            CompletableFuture.allOf(postTask, userTask, pictureTask).join();
            try {
                Page<PostVO> postVOPage = postTask.get();
                Page<Picture> picturePage = pictureTask.get();
                Page<UserVO> userVOPage = userTask.get();
                SearchVO searchVO = new SearchVO();
                searchVO.setPictureList(picturePage.getRecords());
                searchVO.setUserList(userVOPage.getRecords());
                searchVO.setPostList(postVOPage.getRecords());
                return searchVO;
            } catch (Exception e) {
                log.error("查询异常", e);
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查询异常");
            }
        } else {
            SearchVO searchVO = new SearchVO();

            DataSource<?> dataSource = dataSourceRegistry.getDataSourceByType(type);
            Page<?> page = dataSource.doSearch(searchText, current, pageSize);
            searchVO.setDataList(page.getRecords());

            return searchVO;

        }
    }
}
