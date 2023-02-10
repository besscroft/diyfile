package com.besscroft.diyfile.message;

/**
 * @Description 推送服务
 * @Author Bess Croft
 * @Date 2023/2/3 16:31
 */
public interface PushService {

    /**
     * 推送消息到 bark
     * @param sendKey 发送密钥
     * @param message 消息内容
     * @return 结果
     */
    String pushBark(String sendKey, String message);

    /**
     * 推送消息到 server 酱
     * @param sendKey 发送密钥
     * @param message 消息内容
     * @return 结果
     */
    String pushServerChanSimple(String sendKey, String message);

}
