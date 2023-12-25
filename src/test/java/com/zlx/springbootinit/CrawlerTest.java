package com.zlx.springbootinit;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zlx.springbootinit.model.entity.Picture;
import com.zlx.springbootinit.model.entity.Post;
import com.zlx.springbootinit.service.PostService;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class CrawlerTest {

    @Resource
    private PostService postService;

    @SneakyThrows
    @Test
    void testFetchPicture(){
        int currentPage = 1;
        String url =String.format("https://www.bing.com/images/search?q=小黑子&form=QBIR&first=%s&cw=1177&ch=686",currentPage);
        Document doc = Jsoup.connect(url).get();
        System.out.println(doc);
        Elements elements = doc.select(".iuscp.isv");
        List<Picture> pictureList = new ArrayList<Picture>();
        for (Element element : elements) {
            //取图片地址
            String m = element.select(".iusc").get(0).attr("m");
            String title = element.select(".infopt").get(0).select(".inflnk").get(0).attr("aria-label");
            //取图片名称
            Map<String,Object> map = JSONUtil.toBean(m, Map.class);
            String murl = (String)map.get("murl");
            System.out.println(murl);
            System.out.println(title);
            Picture picture = new Picture();
            picture.setUrl(murl);
            picture.setTitle(title);
            pictureList.add(picture);
        }
    }

    @Test
    void testFetchMessage(){
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
        Assertions.assertTrue(b);
    }
}
