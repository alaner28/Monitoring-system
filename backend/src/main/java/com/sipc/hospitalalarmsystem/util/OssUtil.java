package com.sipc.hospitalalarmsystem.util;

import com.qcloud.cos.http.HttpMethodName;
import com.sipc.hospitalalarmsystem.config.OssConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class OssUtil {
    @Autowired
    private OssConfig ossConfig;

    /**
     * 根据uuid获得访问连接 时长365天
     *
     * @param uuid
     * @return {@link String}
     */

    @Cacheable(value = "clipLink", key = "#uuid")
    public String getClipLinkByUuid(String uuid) {
        return "https://citydefender-1326073552.cos.ap-beijing.myqcloud.com/" + uuid + ".flv";
    }
}
