package com.zlx.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlx.springbootinit.common.BaseResponse;
import com.zlx.springbootinit.common.ErrorCode;
import com.zlx.springbootinit.common.ResultUtils;
import com.zlx.springbootinit.exception.BusinessException;
import com.zlx.springbootinit.model.dto.post.PostQueryRequest;
import com.zlx.springbootinit.model.dto.search.SearchRequest;
import com.zlx.springbootinit.model.dto.user.UserQueryRequest;
import com.zlx.springbootinit.model.entity.Picture;
import com.zlx.springbootinit.model.vo.PostVO;
import com.zlx.springbootinit.model.vo.SearchVO;
import com.zlx.springbootinit.model.vo.UserVO;
import com.zlx.springbootinit.service.PictureService;
import com.zlx.springbootinit.service.PostService;
import com.zlx.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 帖子接口
 *
 * @author zlx
 */
@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {

    @Resource
    private UserService userService;

    @Resource
    private PostService postService;

    @Resource
    private PictureService pictureService;

    @PostMapping("/all")
    public BaseResponse<SearchVO> searchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        String searchText = searchRequest.getSearchText();
        CompletableFuture<Page<Picture>> pictureTask = CompletableFuture.supplyAsync(() -> {
            return pictureService.searchPicture(searchText, 1, 10);
        });
        CompletableFuture<Page<UserVO>> userTask = CompletableFuture.supplyAsync(() -> {
            UserQueryRequest userQueryRequest = new UserQueryRequest();
            userQueryRequest.setUserName(searchText);
            return userService.listUserVOByPage(userQueryRequest);
        });
        CompletableFuture<Page<PostVO>> postTask = CompletableFuture.supplyAsync(() -> {
            PostQueryRequest postQueryRequest = new PostQueryRequest();
            postQueryRequest.setSearchText(searchText);
            return postService.listPostVOByPage(postQueryRequest, request);
        });

        CompletableFuture.allOf(postTask,userTask,pictureTask).join();
        try {
            Page<PostVO> postVOPage = postTask.get();
            Page<Picture> picturePage = pictureTask.get();
            Page<UserVO> userVOPage = userTask.get();
            SearchVO searchVO = new SearchVO();
            searchVO.setPictureList(picturePage.getRecords());
            searchVO.setUserList(userVOPage.getRecords());
            searchVO.setPostList(postVOPage.getRecords());
            return ResultUtils.success(searchVO);
        } catch (Exception e) {
            log.error("查询异常",e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"查询异常");
        }
    }
}
