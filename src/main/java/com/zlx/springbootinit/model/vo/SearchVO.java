package com.zlx.springbootinit.model.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zlx.springbootinit.model.entity.Picture;
import com.zlx.springbootinit.model.entity.Post;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 帖子视图
 *
 * @author zlx
 */
@Data
public class SearchVO implements Serializable {

    private final static Gson GSON = new Gson();

    private List<UserVO> userList;
    private List<PostVO> postList;
    private List<Picture> pictureList;
    private List<?> dataList;
}
