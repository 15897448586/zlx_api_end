package com.zlx.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlx.springbootinit.model.entity.InterfaceInfo;
import com.zlx.springbootinit.service.InterfaceInfoService;
import com.zlx.springbootinit.mapper.InterfaceInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author zlx
* @description 针对表【interface_info(接口信息)】的数据库操作Service实现
* @createDate 2024-01-29 22:14:51
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService{

}




