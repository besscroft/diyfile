package com.besscroft.xanadu.message.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.besscroft.xanadu.message.PushService;
import com.besscroft.xanadu.service.SystemConfigService;
import com.ejlchina.okhttps.OkHttps;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @Description 推送服务实现类
 * @Author Bess Croft
 * @Date 2023/2/3 16:39
 */
@Slf4j
@Service
public class PushServiceImpl implements PushService {

    @Override
    public String pushBark(String sendKey, String message) {
        String url = "https://api.day.app" +
                "/" +
                sendKey +
                "/" +
                message;

        JSONObject result = JSONUtil.parseObj(OkHttps.sync(url)
                .get().getBody().toString());
        return Objects.requireNonNull(result.toString());
    }

    @Override
    public String pushServerChanSimple(String sendKey, String message) {
        return "";
    }

}
