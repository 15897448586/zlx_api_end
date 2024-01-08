package com.zlx.springbootinit.datasourse;


import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlx.springbootinit.common.ErrorCode;
import com.zlx.springbootinit.exception.BusinessException;
import com.zlx.springbootinit.model.entity.Picture;
import com.zlx.springbootinit.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 帖子服务实现
 *
 * @author zlx
 */
@Service
@Slf4j
public class PictureDataSource implements DataSource<Picture> {
    @Override
    public Page<Picture> doSearch(String searchText, long pageNum, long pageSize) {
        long current = (pageNum - 1) * pageSize;
        String url = String.format("https://www.bing.com/images/search?q=%s&form=QBIR&first=%s&cw=1177&ch=686", searchText,current);
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据获取异常");
        }
        System.out.println(doc);
        Elements elements = doc.select(".iuscp.isv");
        List<Picture> pictureList = new ArrayList<Picture>();
        for (Element element : elements) {
            //取图片地址
            String m = element.select(".iusc").get(0).attr("m");
            String title = element.select(".infopt").get(0).select(".inflnk").get(0).attr("aria-label");
            //取图片名称
            Map<String, Object> map = JSONUtil.toBean(m, Map.class);
            String murl = (String) map.get("murl");
            Picture picture = new Picture();
            picture.setUrl(murl);
            picture.setTitle(title);
            pictureList.add(picture);
            if(pictureList.size() >= pageSize) {
                break;
            }
        }
        Page<Picture> picturePage = new Page<>(pageNum,pageSize);
        picturePage.setRecords(pictureList);
        return picturePage;
    }

}




