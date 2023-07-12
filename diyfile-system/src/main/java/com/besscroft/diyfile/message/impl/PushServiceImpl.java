package com.besscroft.diyfile.message.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.besscroft.diyfile.message.PushService;
import com.besscroft.diyfile.service.SystemConfigService;
import com.ejlchina.okhttps.OkHttps;
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
@RequiredArgsConstructor
public class PushServiceImpl implements PushService {

    private final SystemConfigService systemConfigService;

    @Override
    public String pushBark(String sendKey, String message) {
        Integer barkStatus = systemConfigService.getBarkStatus();
        if (0 == barkStatus) {
            return "";
        }
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
