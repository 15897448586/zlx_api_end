package com.zlx.springbootinit.job.once;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zlx.springbootinit.esdao.PostEsDao;
import com.zlx.springbootinit.model.dto.post.PostEsDTO;
import com.zlx.springbootinit.model.entity.Post;
import com.zlx.springbootinit.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 获取初始化定时梯子
 *
 * @author zlx
 */
//取消注释，则在springboot重启时执行
//@Component
@Slf4j
public class FetchInitPostList implements CommandLineRunner {

    @Resource
    private PostService postService;

    @Override
    public void run(String... args) {
        String jsonString = "{\"current\":1,\"pageSize\":8,\"sortField\":\"createTime\",\"sortOrder\":\"descend\",\"category\":\"文章\",\"reviewStatus\":1}";
        String url = "https://www.code-nav.cn/api/post/search/page/vo";
        String json = HttpRequest.post(url)
                .body(jsonString)
                .execute()
                .body();
        Map<String,Object> map = JSONUtil.toBean(json, Map.class);
        JSONObject data=(JSONObject)map.get("data");
        JSONArray records = (JSONArray)data.get("records");
        List<Post> postList = new ArrayList<>();
        for (Object record : records) {
            JSONObject tempRecord = (JSONObject)record;
            Post post = new Post();
            post.setTitle(tempRecord.getStr("title"));
            post.setContent(tempRecord.getStr("content"));
            JSONArray tags = (JSONArray) tempRecord.get("tags");
            post.setTags(JSONUtil.toJsonStr(tags.toList(String.class)));
            post.setUserId(1L);
            postList.add(post);
        }
        boolean b = postService.saveBatch(postList);
        if(b) {
            log.info("获取初始化文章数据成功,条数为：{}",postList.size());
        }else{
            log.error("获取帖子失败");
        }
    }
}
