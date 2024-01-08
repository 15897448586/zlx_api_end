package com.zlx.springbootinit.datasourse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlx.springbootinit.common.ErrorCode;
import com.zlx.springbootinit.constant.CommonConstant;
import com.zlx.springbootinit.exception.BusinessException;
import com.zlx.springbootinit.mapper.UserMapper;
import com.zlx.springbootinit.model.dto.user.UserQueryRequest;
import com.zlx.springbootinit.model.entity.User;
import com.zlx.springbootinit.model.enums.UserRoleEnum;
import com.zlx.springbootinit.model.vo.LoginUserVO;
import com.zlx.springbootinit.model.vo.UserVO;
import com.zlx.springbootinit.service.UserService;
import com.zlx.springbootinit.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.zlx.springbootinit.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务实现
 *
 * @author zlx
 */
@Service
@Slf4j
public class UserDataSource implements DataSource<UserVO> {

    @Resource
    private UserService userService;


    @Override
    public Page<UserVO> doSearch(String searchText, long pageNum, long pageSize) {
        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setUserName(searchText);
        userQueryRequest.setCurrent(pageNum);
        userQueryRequest.setPageSize(pageSize);
        Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);
        return userVOPage;
    }
}
