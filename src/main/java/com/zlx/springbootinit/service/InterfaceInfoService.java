package com.zlx.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlx.api_comon.model.entity.InterfaceInfo;

/**
* @author zlx
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-01-29 22:14:51
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}
