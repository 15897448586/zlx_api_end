package com.zlx.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlx.springbootinit.model.entity.UserInterfaceInfo;

import java.util.List;

/**
 * 用户接口信息 Mapper
 *
 * @author zlx
 */
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);
}




