package com.zlx.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.zlx.springbootinit.annotation.AuthCheck;
import com.zlx.springbootinit.common.BaseResponse;
import com.zlx.springbootinit.common.DeleteRequest;
import com.zlx.springbootinit.common.ErrorCode;
import com.zlx.springbootinit.common.ResultUtils;
import com.zlx.springbootinit.constant.UserConstant;
import com.zlx.springbootinit.exception.BusinessException;
import com.zlx.springbootinit.exception.ThrowUtils;
import com.zlx.springbootinit.model.dto.picture.PictureQueryRequest;
import com.zlx.springbootinit.model.dto.post.PostAddRequest;
import com.zlx.springbootinit.model.dto.post.PostEditRequest;
import com.zlx.springbootinit.model.dto.post.PostQueryRequest;
import com.zlx.springbootinit.model.dto.post.PostUpdateRequest;
import com.zlx.springbootinit.model.entity.Picture;
import com.zlx.springbootinit.model.entity.Post;
import com.zlx.springbootinit.model.entity.User;
import com.zlx.springbootinit.model.vo.PostVO;
import com.zlx.springbootinit.service.PictureService;
import com.zlx.springbootinit.service.PostService;
import com.zlx.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 帖子接口
 *
 * @author zlx
 */
@RestController
@RequestMapping("/picture")
@Slf4j
public class PictureController {

    @Resource
    private PictureService pictureService;

    /**
     * 分页获取图片（封装类）
     *
     * @param pictureQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<Picture>> listPictureVOByPage(@RequestBody PictureQueryRequest pictureQueryRequest,
                                                        HttpServletRequest request) {
        long current = pictureQueryRequest.getCurrent();
        long size = pictureQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        String searchText = pictureQueryRequest.getSearchText();
        Page<Picture> picturePage = pictureService.searchPicture(searchText, current, size);
        return ResultUtils.success(picturePage);
    }

}
